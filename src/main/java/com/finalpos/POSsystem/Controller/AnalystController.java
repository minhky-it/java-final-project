package com.finalpos.POSsystem.Controller;

import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Exception.ResponseHandler;
import com.finalpos.POSsystem.Service.AnalystService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RestController
@ResponseBody
@RequestMapping("/api/orders-analyst")
public class AnalystController {


    @Autowired
    AnalystService service;

    @GetMapping("/allOrders")
    public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String token) {
        try{
            return ResponseHandler.builder("OK", service.orders(token));
        }
        catch (Exception e){
            throw new FailedException("Failed at analyst controller: " + e.getMessage());
        }
    }

    @GetMapping("/byDay")
    public ResponseEntity<?> getOrdersByDay(@RequestParam("startDate") String startDate,
                                  @RequestParam("endDate") String endDate,
                                  @RequestHeader("Authorization") String token) {
        try{
            return ResponseHandler.builder("OK", service.ordersByDate(startDate,endDate,token));
        }
        catch (Exception e){
            throw new FailedException("Failed to get orders by date: " + e.getMessage());
        }
    }

}
