package com.finalpos.POSsystem.Service;

import com.finalpos.POSsystem.Entity.CustomerEntity;
import com.finalpos.POSsystem.Entity.OrderDetailEntity;
import com.finalpos.POSsystem.Entity.OrderEntity;
import com.finalpos.POSsystem.Entity.Package;
import com.finalpos.POSsystem.Entity.UserEntity;
import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Model.DTO.OrderDTO;
import com.finalpos.POSsystem.Model.Mapstruct.CustomerMapper;
import com.finalpos.POSsystem.Model.Mapstruct.OrderDetailMapper;
import com.finalpos.POSsystem.Model.Mapstruct.OrderMapper;
import com.finalpos.POSsystem.Model.Mapstruct.UserMapper;
import com.finalpos.POSsystem.Repository.CustomerRepository;
import com.finalpos.POSsystem.Repository.OrderDetailModelRepository;
import com.finalpos.POSsystem.Repository.OrderRepository;
import com.finalpos.POSsystem.Repository.UserRepository;
import com.finalpos.POSsystem.Service.Interface.OrderInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersService implements OrderInterface {
    @Autowired
    OrderRepository orderDb;
    @Autowired
    CustomerRepository customerDb;
    @Autowired
    UserRepository userDb;
    @Autowired
    OrderDetailModelRepository orderDetailDb;

    OrderMapper mapper = OrderMapper.INSTANCE;
    CustomerMapper customerMapper = CustomerMapper.INSTANCE;
    UserMapper userMapper = UserMapper.INSTANCE;
    OrderDetailMapper orderDetailMapper = OrderDetailMapper.INSTANCE;

    @Override
    public Map<String, Object> ordersByOrdNumber(String order_number) {
        try{
            OrderEntity orderDB = orderDb.findByOrderNumber(order_number);
            if(orderDB == null)
                throw new FailedException("Order does not exist");

            String customer_id = orderDB.getCustomerId();
            String staff_id = orderDB.getStaffId();

            CustomerEntity customerDB = customerDb.findCustomerById(customer_id);
            UserEntity staffDB = userDb.findUserModelById(staff_id);
            OrderDetailEntity order_detailDB  = orderDetailDb.findByOrderNumber(order_number);

            Map<String, Object> data = new HashMap<>();
            data.put("order", mapper.toDTO(orderDB));
            data.put("customer", customerMapper.toDTO(customerDB));
            data.put("staff", userMapper.toDTO(staffDB));
            data.put("orderDetail", orderDetailMapper.toDTO(order_detailDB));
            return data;

        }catch (Exception e){
            throw new FailedException("Error in order service: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> orders(int page, int size) {
        try{

            int skipAmount = (page - 1) * size;
            int totalOrders = (int) orderDb.count();
            int totalPages = (int) Math.ceil((double) totalOrders / size);


            List<OrderEntity> orderList = orderDb.findAll();
            List<OrderDTO> order = new ArrayList<>();

            int endIdx = Math.min(skipAmount + size, orderList.size());
            for (int i = skipAmount; i < endIdx; i++) {
                order.add(mapper.toDTO(orderList.get(i)));
            }
            Map<String, Object> data = new HashMap<>();
            data.put("orders", order);
            data.put("divider", totalPages);
            return data;
        } catch (Exception e) {
            throw new FailedException("Error in order service: " + e.getMessage());
        }
    }
}
