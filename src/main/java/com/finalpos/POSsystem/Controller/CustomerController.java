package com.finalpos.POSsystem.Controller;
import com.finalpos.POSsystem.Entity.CustomerEntity;
import com.finalpos.POSsystem.Entity.OrderEntity;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Exception.ResponseHandler;
import com.finalpos.POSsystem.Repository.CustomerRepository;
import com.finalpos.POSsystem.Entity.Package;
import com.finalpos.POSsystem.Repository.OrderRepository;
import com.finalpos.POSsystem.Service.CustomerService;
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
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    CustomerService service;
    @GetMapping("/")  // Đạt
    private ResponseEntity<?> getAllCustomers(@RequestParam Optional<String> page){
        try {
            int pageNumber = Integer.parseInt(page.orElse("1"));
            return ResponseHandler.builder("OK", service.customers(pageNumber, 10));
        }catch (Exception e){
            throw new FailedException("Failed to get customers: " + e.getMessage());
        }
    }

    @GetMapping("/{id}") // Đạt
    private ResponseEntity<?> getCustomerById(@PathVariable("id") String id){
        try {
            return ResponseHandler.builder("OK", service.detail(id));
        }catch (Exception e){
            throw new FailedException("Failed to get detail: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/transactions") // Đạt
    private ResponseEntity<?> getTransactionsByCustomerId(@PathVariable("id") String id, @RequestParam Optional<String> page){
        try {
            int pageNumber = Integer.parseInt(page.orElse("1"));
            return ResponseHandler.builder("OK", service.transactions(id,pageNumber,10));
        }catch (Exception e){
            throw new FailedException("Failed to get transactions: " + e.getMessage());
        }
    }
}