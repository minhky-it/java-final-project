package com.finalpos.POSsystem.Service;

import com.finalpos.POSsystem.Entity.*;
import com.finalpos.POSsystem.Entity.Package;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Model.Mapstruct.OrderMapper;
import com.finalpos.POSsystem.Repository.OrderDetailModelRepository;
import com.finalpos.POSsystem.Repository.OrderRepository;
import com.finalpos.POSsystem.Repository.ProductRepository;
import com.finalpos.POSsystem.Repository.UserRepository;
import com.finalpos.POSsystem.Service.Interface.AnalystInterface;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.finalpos.POSsystem.Service.AccountService.JWT_Key;

public class AnalystService implements AnalystInterface {
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    OrderDetailModelRepository orderDetailRepo;
    @Autowired
    ProductRepository productRepo;

    @Autowired
    UserRepository userRepo;

    OrderMapper mapper = OrderMapper.INSTANCE;

    @Override
    public Map<String, Object> orders(String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token).getBody();
            String username = claims.get("username", String.class);

            UserEntity user = userRepo.findByUsername(username);

            if(user == null)
                throw new FailedException("User not found");

            List<OrderEntity> ordersDB = orderRepo.findAll();


            int totalPrice_temp = totalPriceOfOrder(ordersDB);
            int totalProducts_temp = totalNumberOfProducts(ordersDB);
            if(user.getRole().equals("Administrator")){
                int totalProfit_temp = calculateProfit(ordersDB);
                Map<String, Object> data = new HashMap<>();
                data.put("orders", ordersDB.stream().map(mapper::toDTO));
                data.put("totalProfit", totalProfit_temp);
                data.put("totalPrice", totalPrice_temp);
                data.put("totalProducts", totalProducts_temp);
                return data;
            }
            else{
                Map<String, Object> data = new HashMap<>();
                data.put("orders", ordersDB.stream().map(mapper::toDTO));
                data.put("totalPrice_temp", totalPrice_temp);
                data.put("totalProducts", totalProducts_temp);
                return data;
            }

        } catch (Exception e) {
            throw new FailedException("Error in analyst service: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> ordersByDate(String startDate, String endDate, String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(JWT_Key).parseClaimsJws(token).getBody();
            String username = claims.get("username", String.class);

            UserEntity user = userRepo.findByUsername(username);

            if(user == null)
                throw new FailedException("User not found!");

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

            int totalPrice_temp = totalPriceOfOrder(ordersTime);
            int totalProducts_temp = totalNumberOfProducts(ordersTime);

            Map<String, Object> data = new HashMap<>();
            if(user.getRole().equals("Administrator")){
                int totalProfit_temp = calculateProfit(ordersTime);
                data.put("orders", ordersTime.stream().map(mapper::toDTO));
                data.put("totalProfit", totalProfit_temp);
                data.put("totalPrice", totalPrice_temp);
                data.put("totalProducts", totalProducts_temp);
                return data;
            }
            else{
                data.put("orders", ordersTime.stream().map(mapper::toDTO));
                data.put("totalPrice", totalPrice_temp);
                data.put("totalProducts", totalProducts_temp);
                return data;
            }

        }catch (Exception e){
            throw new FailedException("Error in analyst service: " + e.getMessage());
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
