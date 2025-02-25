package com.finalpos.POSsystem.Repository;

import com.finalpos.POSsystem.Entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    OrderEntity findByOrderNumber(String order_number);
    List<OrderEntity> findByCustomerId(String customer_id);

}
