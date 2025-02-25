package com.finalpos.POSsystem.Repository;

import com.finalpos.POSsystem.Entity.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<CustomerEntity, String>{
    CustomerEntity findByPhone(String phone);
    CustomerEntity findCustomerById(String id);
}
