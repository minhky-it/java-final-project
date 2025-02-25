package com.finalpos.POSsystem.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.finalpos.POSsystem.Entity.*;
import com.finalpos.POSsystem.Entity.Package;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalpos.POSsystem.Repository.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import static com.finalpos.POSsystem.Controller.AccountController.JWT_Key;

@Controller
@RestController
@ResponseBody
@RequestMapping("/api/pos")
public class POSController {
    @Value("${default.application.avatar}")
    private String url;
    @Autowired
    CustomerRepository cusDb;
    @Autowired
    ProductRepository proDb;
    @Autowired
    UserRepository userDb;
    @Autowired
    OrderRepository ordDb;
    @Autowired
    OrderDetailModelRepository ordDetailDb;
    @Autowired
    ProductCartRepository proCartDb;

    @GetMapping("/find-customer/{phone}") // An Nguyen
    private Package findCustomerByPhone(@PathVariable("phone") String phone){
        try {
            CustomerEntity customer = cusDb.findByPhone(phone);
            if (customer != null) {
                return new Package(0, "User exist",customer);
            } else
                return new Package(404, "User not exist",customer);
        }catch (Exception e){
            return new Package(404, e.getMessage(), null);
        }
    }

    @PostMapping("/create-customer") // An Nguyen
    private Package createCustomer(@RequestParam String phone,
                                   @RequestParam String name,
                                   @RequestParam String address){
        try {
            CustomerEntity customerModel = new CustomerEntity(name,phone,address,url);

            if (cusDb.findByPhone(phone) != null) {
                return new Package(404, "User already exist", customerModel);
            } else {
                cusDb.save(customerModel);
                return new Package(0, "success", customerModel);
            }
        }catch (Exception e){
            return new Package(404, e.getMessage(), null);
        }
    }

    @PostMapping("/create-a-bill") // Đạt
    private Package createABill(@RequestBody PaymentModel payment) {
        try {
            // Get data from RequestBody
            List<CartItem> carts = extractCart(payment.getCart());
            Customer cus = extractCustomer(payment.getCustomer());

            // Customer session
            CustomerEntity customerModel = cusDb.findByPhone(cus.getPhone());
            if(customerModel == null) {
                if(!cus.getPhone().isEmpty() &&
                        !cus.getName().isEmpty() &&
                        !cus.getAddress().isEmpty()) {
                    customerModel = new CustomerEntity(cus.getName(), cus.getPhone(), cus.getAddress(), url);
                    cusDb.save(customerModel);
                } else {
                    return new Package(403, "The data of the customer is not valid", null);
                }
            }

            // Staff session
            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(payment.getToken()).getBody();
            String _id = claims.get("_id", String.class);

            // Calculate bill
            int sub_total = 0, count = 0;
            ArrayList<ProductCartEntity> productCartModels = new ArrayList<>();
            for(CartItem cart : carts ) {
                ProductCartEntity productCartModel = new ProductCartEntity();
                ProductEntity product = proDb.findByBarcode(cart.getBarcode());
                product.setPurchase(true);
                int amount = Integer.parseInt(String.valueOf(cart.getAmount()));
                double retail_price = product.getRetail_price();
                sub_total += (int) Math.round(amount * retail_price);
                count += amount;
                productCartModel.setName(product.getName());
                productCartModel.setQuantity(amount);
                productCartModel.setBarcode(product.getBarcode());
                productCartModel.setRetail_price((int) product.getRetail_price());
                productCartModel.setImport_price((int) product.getImport_price());
                productCartModel = proCartDb.save(productCartModel);
                productCartModels.add(productCartModel);
                proDb.save(product);
            }
            int tax_fee = sub_total * payment.getTaxrate() / 100;
            int total = sub_total + tax_fee;
            int change = payment.getCash() - total;
            String order_number = java.time.LocalTime.now()
                    .toString()
                    .replace(".", "")
                    .replace(":", "")
                    .substring(0, 12);

            if(change < 0) {
                return new Package(403, "The cash is not enough", null);
            }

            // Create a bill
            OrderEntity orderModel = new OrderEntity();
            orderModel.setOrderNumber(order_number);
            orderModel.setCustomerId(cusDb.findByPhone(customerModel.getPhone()).getId());
            orderModel.setStaffId(_id);
            orderModel.setTaxrate(payment.getTaxrate());
            orderModel.setTaxfee(tax_fee);
            orderModel.setSub_total(sub_total);
            orderModel.setCash(payment.getCash());
            orderModel.setChange(change);
            orderModel.setTotal(total);
            orderModel.setQuantity(count);
            orderModel.setPaymentMethod(cus.getPaymentMethod());
            orderModel.setCreated_date(String.valueOf(java.time.LocalDateTime.now()));
            ordDb.save(orderModel);

            // Create a detail bill
            OrderDetailEntity orderDetailModel = new OrderDetailEntity();
            orderDetailModel.setOrder_id(orderModel.getId());
            orderDetailModel.setOrderNumber(orderModel.getOrderNumber());
            orderDetailModel.setProducts(productCartModels);
            ordDetailDb.save(orderDetailModel);




            return new Package(0, "Success", orderModel);
        } catch (Exception e) {
            return new Package(404, e.getMessage(), null);
        }
    }

    @GetMapping("/search-product/{barcode}") // An Nguyen
    private Package searchProduct(@PathVariable("barcode") String barcode){
        try {
            ProductEntity product = proDb.findByBarcode(barcode);

            if (product != null) {
                return new Package(0, "Success", product);
            } else {
                return new Package(404, "Product not found", null);
            }
        }catch (Exception e){
            return new Package(404, e.getMessage(), null);
        }
    }

    @GetMapping("/search-products") // An Nguyen
    private Package searchProducts(@RequestParam("terms") String terms){
        try {
            List<ProductEntity> productModelList = proDb.findByTerm(terms);
            return new Package(0, "success", productModelList);
        }catch (Exception e){
            return new Package(404, e.getMessage(), null);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class PaymentModel {
        private int taxrate;
        private String customer;
        private String cart;
        private int cash;
        private String token;

        public int getTaxrate() {
            return taxrate;
        }

        public void setTaxrate(int taxrate) {
            this.taxrate = taxrate;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getCart() {
            return cart;
        }

        public void setCart(String cart) {
            this.cart = cart;
        }

        public int getCash() {
            return cash;
        }

        public void setCash(int cash) {
            this.cash = cash;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Customer{
        private String name;
        private String phone;
        private String address;
        private int paymentMethod;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(int paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CartItem {
        private String id;
        private String barcode;
        private String name;
        private int quantity;
        private String description;
        private int import_price;
        private int retail_price;
        private String image;
        private String category;
        private String creation_date;
        private boolean purchase;
        private int amount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getImport_price() {
            return import_price;
        }

        public void setImport_price(int import_price) {
            this.import_price = import_price;
        }

        public int getRetail_price() {
            return retail_price;
        }

        public void setRetail_price(int retail_price) {
            this.retail_price = retail_price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCreation_date() {
            return creation_date;
        }

        public void setCreation_date(String creation_date) {
            this.creation_date = creation_date;
        }

        public boolean isPurchase() {
            return purchase;
        }

        public void setPurchase(boolean purchase) {
            this.purchase = purchase;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

    private Customer extractCustomer(String customer) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(customer, Customer.class);
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }
    private List<CartItem> extractCart(String cart) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(cart, new TypeReference<List<CartItem>>() {});
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}