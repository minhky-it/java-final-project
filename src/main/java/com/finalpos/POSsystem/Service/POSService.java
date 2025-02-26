package com.finalpos.POSsystem.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalpos.POSsystem.Controller.POSController;
import com.finalpos.POSsystem.Entity.*;
import com.finalpos.POSsystem.Entity.Package;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Model.DTO.CustomerDTO;
import com.finalpos.POSsystem.Model.DTO.OrderDTO;
import com.finalpos.POSsystem.Model.DTO.ProductDTO;
import com.finalpos.POSsystem.Model.Mapstruct.CustomerMapper;
import com.finalpos.POSsystem.Model.Mapstruct.OrderMapper;
import com.finalpos.POSsystem.Model.Mapstruct.ProductMapper;
import com.finalpos.POSsystem.Repository.*;
import com.finalpos.POSsystem.Service.Interface.POSInterface;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.finalpos.POSsystem.Service.AccountService.JWT_Key;

public class POSService implements POSInterface {
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

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;
    OrderMapper orderMapper = OrderMapper.INSTANCE;
    ProductMapper productMapper = ProductMapper.INSTANCE;
    @Override
    public CustomerDTO customer(String phone) {
        try{
            CustomerEntity customer = cusDb.findByPhone(phone);

            if (customer == null)
                throw new FailedException("Customer does not exist!");
            return customerMapper.toDTO(customer);
        }catch (Exception e){
            throw new FailedException("Error in POS service: " + e.getMessage());
        }
    }


    @Override
    public CustomerDTO createCustomer(String phone, String name, String address) {
        try{
            CustomerEntity customerModel = new CustomerEntity(name,phone,address,url);

            if (cusDb.findByPhone(phone) != null)
                throw new FailedException("User already exist!");
            cusDb.save(customerModel);
            return customerMapper.toDTO(customerModel);
        }catch (Exception e){
            throw new FailedException("Error in POS service: " + e.getMessage());
        }
    }

    @Override
    public OrderDTO createBill(POSController.PaymentModel payment) {
        try{
            // Get data from RequestBody
            List<POSController.CartItem> carts = extractCart(payment.getCart());
            POSController.Customer cus = extractCustomer(payment.getCustomer());

            // Customer session
            CustomerEntity customerModel = cusDb.findByPhone(cus.getPhone());
            if(customerModel == null) {
                if(!cus.getPhone().isEmpty() &&
                        !cus.getName().isEmpty() &&
                        !cus.getAddress().isEmpty()) {
                    customerModel = new CustomerEntity(cus.getName(), cus.getPhone(), cus.getAddress(), url);
                    cusDb.save(customerModel);
                } else
                    throw new FailedException("The data of the customer is not valid!");
            }

            // Staff session
            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(payment.getToken()).getBody();
            String _id = claims.get("_id", String.class);

            // Calculate bill
            int sub_total = 0, count = 0;
            ArrayList<ProductCartEntity> productCartModels = new ArrayList<>();
            for(POSController.CartItem cart : carts ) {
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

            if(change < 0)
                throw new FailedException("The cash is not enough!");

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

            return orderMapper.toDTO(orderModel);
        }catch (Exception e){
            throw new FailedException("Error in POS service: " + e.getMessage());
        }
    }


    @Override
    public ProductDTO searchProduct(String barcode) {
        try{
            ProductEntity product = proDb.findByBarcode(barcode);
            return productMapper.toDTO(product);
        }catch (Exception e){
            throw new FailedException("Error in POS service: " + e.getMessage());
        }
    }

    @Override
    public List<ProductDTO> products(String terms) {
        try{
            List<ProductEntity> productModelList = proDb.findByTerm(terms);
            return productModelList.stream().map(productMapper::toDTO).toList();
        } catch (Exception e) {
            throw new FailedException("Error in POS service: " + e.getMessage());
        }
    }

    private POSController.Customer extractCustomer(String customer) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(customer, POSController.Customer.class);
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }
    private List<POSController.CartItem> extractCart(String cart) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(cart, new TypeReference<List<POSController.CartItem>>() {});
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
