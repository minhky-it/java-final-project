package com.finalpos.POSsystem.Repository;

import com.finalpos.POSsystem.Entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
    UserEntity findUserModelById(String userId);
    UserEntity removeUserModelById(String userId);

    UserEntity findByEmail(String email);
}
