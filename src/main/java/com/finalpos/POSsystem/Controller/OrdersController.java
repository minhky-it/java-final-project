package com.finalpos.POSsystem.Controller;

import com.finalpos.POSsystem.Entity.*;
import com.finalpos.POSsystem.Entity.Package;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Exception.ResponseHandler;
import com.finalpos.POSsystem.Repository.CustomerRepository;
import com.finalpos.POSsystem.Repository.OrderDetailModelRepository;
import com.finalpos.POSsystem.Repository.OrderRepository;
import com.finalpos.POSsystem.Repository.UserRepository;
import com.finalpos.POSsystem.Service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
@ResponseBody
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    OrdersService service;

    @GetMapping("/{order_number}") // Đạt
    private ResponseEntity<?> getOrderByOrderNumber(@PathVariable("order_number") String order_number){
        try {
            return ResponseHandler.builder("OK", service.ordersByOrdNumber(order_number));
        }catch (Exception e){
            throw new FailedException("Failed at order controller: " + e.getMessage());
        }
    }

    @GetMapping("/") // Đạt
    private ResponseEntity<?> getAllOrders(@RequestParam Optional<String> page){
        try {
            int pageNumber = Integer.parseInt(page.orElse("1"));
            return ResponseHandler.builder("OK", service.orders(pageNumber,10));
        }catch (Exception e){
            throw new FailedException("Failed at order controller: " + e.getMessage());
        }
    }
}