package com.finalpos.POSsystem.Repository;

import com.finalpos.POSsystem.Entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    ProductEntity findByBarcode(String barcode);
    ProductEntity removeProductModelByBarcode(String barcode);

    @Query("{'$or':[{'name': {$regex : ?0, $options: 'i'}}, {'description': {$regex : ?0, $options: 'i'}}, {'category': {$regex : ?0, $options: 'i'}}]}")
    List<ProductEntity> findByTerm(String term);
}
