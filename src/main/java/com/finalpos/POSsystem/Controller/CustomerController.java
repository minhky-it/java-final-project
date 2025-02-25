package com.finalpos.POSsystem.Controller;
import com.finalpos.POSsystem.Entity.CustomerEntity;
import com.finalpos.POSsystem.Entity.OrderEntity;
import com.finalpos.POSsystem.Repository.CustomerRepository;
import com.finalpos.POSsystem.Entity.Package;
import com.finalpos.POSsystem.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    CustomerRepository cusDb;

    @Autowired
    OrderRepository ordDb;

    @GetMapping("/")  // Đạt
    private Package getAllCustomers(@RequestParam Optional<String> page){
        try {
            int pageSize = 10;
            int pageNumber = 1;
            if(!page.isEmpty() && page.get() != "null") {
                pageNumber = Integer.parseInt(page.get());
            }
            int skipAmount = (pageNumber - 1) * pageSize;
            int totalUsers = (int) cusDb.count();
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

            List<CustomerEntity> customerLists = cusDb.findAll();
            List<CustomerEntity> customer = new ArrayList<>();

            int endIdx = Math.min(skipAmount + pageSize, customerLists.size());
            for (int i = skipAmount; i < endIdx; i++) {
                customer.add(customerLists.get(i));
            }
            Object data = new Object() {
                public final List<CustomerEntity> customers = customer;
                public final int divider = totalPages;
            };
            return new Package(0, "success", data);
        }catch (Exception e){
            return new Package(404, e.getMessage(), null);
        }
    }

    @GetMapping("/{id}") // Đạt
    private Package getCustomerById(@PathVariable("id") String id){
        try {
            CustomerEntity customer = cusDb.findCustomerById(id);
            return new Package(0, "success", customer);
        }catch (Exception e){
            return new Package(404, e.getMessage(), null);
        }
    }

    @GetMapping("/{id}/transactions") // Đạt
    private Package getTransactionsByCustomerId(@PathVariable("id") String id, @RequestParam Optional<String> page){
        try {
            int pageSize = 10;
            int pageNumber = 1;
            if(!page.isEmpty() && page.get() != "null") {
                pageNumber = Integer.parseInt(page.get());
            }
            int skipAmount = (pageNumber - 1) * pageSize;
            int totalOrders = (int) ordDb.count();
            int totalPages = (int) Math.ceil((double) totalOrders / pageSize);


            List<OrderEntity> orderList = ordDb.findByCustomerId(id);
            List<OrderEntity> order = new ArrayList<>();

            // Check num of users in the last page
            // It will continue() when page + 1 (skipAmount > size()) -> reduce run time
            int endIdx = Math.min(skipAmount + pageSize, orderList.size());
            for (int i = skipAmount; i < endIdx; i++) {
                order.add(orderList.get(i));
            }

            CustomerEntity customerDB = cusDb.findCustomerById(id);
            Object data = new Object() {
                public final List<OrderEntity> transactions = order;
                public final int divider = totalPages;
                public final CustomerEntity customer = customerDB;
            };
            return new Package(0, "success", data);
        }catch (Exception e){
            return new Package(404, e.getMessage(), null);
        }
    }
}