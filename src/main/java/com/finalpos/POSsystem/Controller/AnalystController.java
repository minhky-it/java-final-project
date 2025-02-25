package com.finalpos.POSsystem.Controller;


import com.finalpos.POSsystem.Entity.*;
import com.finalpos.POSsystem.Entity.Package;
import com.finalpos.POSsystem.Repository.OrderDetailModelRepository;
import com.finalpos.POSsystem.Repository.OrderRepository;
import com.finalpos.POSsystem.Repository.ProductRepository;
import com.finalpos.POSsystem.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.finalpos.POSsystem.Controller.AccountController.JWT_Key;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RestController
@ResponseBody
@RequestMapping("/api/orders-analyst")
public class AnalystController {

    @Autowired
    OrderRepository orderRepo;
    @Autowired
    OrderDetailModelRepository orderDetailRepo;
    @Autowired
    ProductRepository productRepo;

    @Autowired
    UserRepository userRepo;


    @GetMapping("/allOrders")
    public Package getAllOrders(@RequestHeader("Authorization") String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token).getBody();
            String username = claims.get("username", String.class);

            UserEntity user = userRepo.findByUsername(username);

            if(user == null){
                return new Package(404, "User not found", null);
            }

            List<OrderEntity> ordersDB = orderRepo.findAll();

            if(ordersDB == null){
                return new Package(404, "No orders found", null);
            }
            else{
                int totalPrice_temp = totalPriceOfOrder(ordersDB);
                int totalProducts_temp = totalNumberOfProducts(ordersDB);
                if(user.getRole().equals("Administrator")){
                    int totalProfit_temp = calculateProfit(ordersDB);
                    Object data = new Object(){
                        public List<OrderEntity> orders = ordersDB;
                        public int totalProfit = totalProfit_temp;
                        public int totalPrice = totalPrice_temp;
                        public int totalProducts = totalProducts_temp;
                    };
                    return new Package(0, "success", data);
                }
                else{
                    Object data = new Object(){
                        public List<OrderEntity> orders = ordersDB;
                        public int totalPrice = totalPrice_temp;
                        public int totalProducts = totalProducts_temp;
                    };
                    return new Package(0, "success", data);
                }
            }
        }
        catch (Exception e){
            return new Package(405, e.getMessage(), null);
        }
    }

    @GetMapping("/byDay")
    public Package getOrdersByDay(@RequestParam("startDate") String startDate,
                                  @RequestParam("endDate") String endDate,
                                  @RequestHeader("Authorization") String token) {
        try{
            System.out.println("startDate: " + startDate);
            System.out.println("endDate: " + endDate);

            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token).getBody();
            String username = claims.get("username", String.class);

            UserEntity user = userRepo.findByUsername(username);

            if(user == null){
                return new Package(404, "User not found", null);
            }
            Map<String, String> req = new HashMap<>();
            req.put("startDate", startDate);
            req.put("endDate", endDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date start = null;
            Date end = null;
            if (req.containsKey("startDate")) {
                start = dateFormat.parse(req.get("startDate"));
            }

            if (req.containsKey("endDate")) {
                end = dateFormat.parse(req.get("endDate"));
            }
            List <OrderEntity> ordersDB = orderRepo.findAll();

            List<OrderEntity> ordersTime = new ArrayList<>();
            for (OrderEntity order : ordersDB) {
                Date date = dateFormat.parse(order.getCreated_date());
                if (start != null && end != null) {
                    if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {
                        ordersTime.add(order);
                    }
                } else if (start != null) {
                    if (date.compareTo(start) >= 0) {
                        ordersTime.add(order);
                    }
                } else if (end != null) {
                    if (date.compareTo(end) <= 0) {
                        ordersTime.add(order);
                    }
                }
            }


            if(ordersDB == null){
                return new Package(404, "No orders found", null);
            }
            else{

                int totalPrice_temp = totalPriceOfOrder(ordersTime);
                int totalProducts_temp = totalNumberOfProducts(ordersTime);
                if(user.getRole().equals("Administrator")){
                    int totalProfit_temp = calculateProfit(ordersTime);
                    Object data = new Object(){
                        public List<OrderEntity> orders = ordersTime;
                        public int totalProfit = totalProfit_temp;
                        public int totalPrice = totalPrice_temp;
                        public int totalProducts = totalProducts_temp;
                    };
                    return new Package(0, "success", data);
                }
                else{
                    Object data = new Object(){
                        public List<OrderEntity> orders = ordersTime;
                        public int totalPrice = totalPrice_temp;
                        public int totalProducts = totalProducts_temp;
                    };
                    return new Package(0, "success", data);
                }
            }

        }
        catch (Exception e){
            return new Package(405, e.getMessage(), null);
        }
    }

    // Sub functions
    private int totalPriceOfOrder(List<OrderEntity> orders){
        int totalPrice = 0;
        for (OrderEntity order : orders) {
            totalPrice += order.getTotal();
        }
        return totalPrice;
    }

    private int totalNumberOfProducts(List<OrderEntity> orders){
        int totalNumberOfProducts = 0;
        for (OrderEntity order : orders) {
            totalNumberOfProducts += order.getQuantity();
        }
        return totalNumberOfProducts;
    }

    private int calculateProfit(List<OrderEntity> orders) {
        int profit = 0;

        System.out.println("calculateProfit");
        for (OrderEntity order : orders) {
            OrderDetailEntity orderDetail = orderDetailRepo.findByOrderNumber(order.getOrderNumber());
            System.out.println("orderDetail");
            if (orderDetail == null) {
                profit += order.getTotal();
            } else {
                System.out.println("productCart");
                for (ProductCartEntity productCart : orderDetail.getProducts()){
                    System.out.println("product");
                   ProductEntity product = productRepo.findByBarcode(productCart.getBarcode());

                   if (product != null) {
                       int profitPerProduct = (int) (productCart.getQuantity() * (product.getRetail_price() - product.getImport_price()));
                        profit += profitPerProduct;
                   }
                }
            }
        }

        return profit;
    }

}
