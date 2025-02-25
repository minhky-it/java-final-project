package com.finalpos.POSsystem.Repository;

import com.finalpos.POSsystem.Entity.ProductCartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductCartRepository extends MongoRepository<ProductCartEntity, String> {

}
