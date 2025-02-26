package com.finalpos.POSsystem.Service;

import com.finalpos.POSsystem.Entity.CustomerEntity;
import com.finalpos.POSsystem.Entity.OrderEntity;
import com.finalpos.POSsystem.Entity.Package;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Model.DTO.CustomerDTO;
import com.finalpos.POSsystem.Model.DTO.OrderDTO;
import com.finalpos.POSsystem.Model.Mapstruct.CustomerMapper;
import com.finalpos.POSsystem.Model.Mapstruct.OrderMapper;
import com.finalpos.POSsystem.Repository.CustomerRepository;
import com.finalpos.POSsystem.Repository.OrderRepository;
import com.finalpos.POSsystem.Service.Interface.CustomerInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerService implements CustomerInterface {
    @Autowired
    CustomerRepository cusDb;

    @Autowired
    OrderRepository ordDb;

    CustomerMapper mapper = CustomerMapper.INSTANCE;
    OrderMapper orderMapper = OrderMapper.INSTANCE;
    @Override
    public Map<String, Object> customers(int page, int size) {
        try{
            int skipAmount = (page - 1) * size;
            int totalUsers = (int) cusDb.count();
            int totalPages = (int) Math.ceil((double) totalUsers / size);

            List<CustomerEntity> customerLists = cusDb.findAll();
            List<CustomerEntity> customer = new ArrayList<>();

            int endIdx = Math.min(skipAmount + size, customerLists.size());
            for (int i = skipAmount; i < endIdx; i++) {
                customer.add(customerLists.get(i));
            }
            Map<String, Object> result = new HashMap<>();
            result.put("customers", customer.stream().map(mapper::toDTO));
            result.put("divider", totalPages);
            return result;
        } catch (Exception e) {
            throw new FailedException("Error in customer service: " + e.getMessage());
        }
    }

    @Override
    public CustomerDTO detail(String id) {
        try{
            CustomerEntity customer = cusDb.findCustomerById(id);
            return mapper.toDTO(customer);
        } catch (Exception e) {
            throw new FailedException("Error in customer service: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> transactions(String id, int page, int size) {
        try{
            int skipAmount = (page - 1) * size;
            int totalOrders = (int) ordDb.count();
            int totalPages = (int) Math.ceil((double) totalOrders / size);


            List<OrderEntity> orderList = ordDb.findByCustomerId(id);
            List<OrderDTO> order = new ArrayList<>();

            // Check num of users in the last page
            // It will continue() when page + 1 (skipAmount > size()) -> reduce run time
            int endIdx = Math.min(skipAmount + size, orderList.size());
            for (int i = skipAmount; i < endIdx; i++) {
                order.add(orderMapper.toDTO(orderList.get(i)));
            }

            CustomerEntity customerDB = cusDb.findCustomerById(id);

            Map<String, Object> result = new HashMap<>();
            result.put("transactions", order);
            result.put("divider", totalPages);
            result.put("customer", mapper.toDTO(customerDB));
            return result;
        }catch (Exception e){
            throw new FailedException("Error in customer service: " + e.getMessage());
        }
    }
}
