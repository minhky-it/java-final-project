package com.finalpos.POSsystem.Controller;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Exception.ResponseHandler;
import com.finalpos.POSsystem.Service.POSService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@ResponseBody
@RequestMapping("/api/pos")
public class POSController {

    @Autowired
    POSService service;

    @GetMapping("/find-customer/{phone}") // An Nguyen
    private ResponseEntity<?> findCustomerByPhone(@PathVariable("phone") String phone){
        try {
            return ResponseHandler.builder("OK", service.customer(phone));
        }catch (Exception e){
            throw new FailedException("Failed at POS controller: " + e.getMessage());
        }
    }

    @PostMapping("/create-customer") // An Nguyen
    private ResponseEntity<?> createCustomer(@RequestParam String phone,
                                   @RequestParam String name,
                                   @RequestParam String address){
        try {
            return ResponseHandler.builder("OK", service.createCustomer(phone, name, address));
        }catch (Exception e){
            throw new FailedException("Failed at POS controller: " + e.getMessage());
        }
    }

    @PostMapping("/create-a-bill") // Đạt
    private ResponseEntity<?> createABill(@RequestBody PaymentModel payment) {
        try {
            return ResponseHandler.builder("OK", service.createBill(payment));
        } catch (Exception e) {
            throw new FailedException("Failed at POS controller: " + e.getMessage());
        }
    }

    @GetMapping("/search-product/{barcode}") // An Nguyen
    private ResponseEntity<?> searchProduct(@PathVariable("barcode") String barcode){
        try {
            return ResponseHandler.builder("OK", service.searchProduct(barcode));
        }catch (Exception e){
            throw new FailedException("Failed at POS controller: " + e.getMessage());
        }
    }

    @GetMapping("/search-products") // An Nguyen
    private ResponseEntity<?> searchProducts(@RequestParam("terms") String terms){
        try {
            return ResponseHandler.builder("OK", service.products(terms));
        }catch (Exception e){
            throw new FailedException("Failed at POS controller: " + e.getMessage());
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
    public static class CartItem {
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


}