package com.finalpos.POSsystem.Repository;

import com.finalpos.POSsystem.Entity.OrderDetailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderDetailModelRepository extends MongoRepository<OrderDetailEntity, String> {


    OrderDetailEntity findByOrderNumber(String orderNumber);
}
