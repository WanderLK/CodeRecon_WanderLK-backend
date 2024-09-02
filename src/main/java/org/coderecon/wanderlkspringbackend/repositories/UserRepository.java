package org.coderecon.wanderlkspringbackend.repositories;

import org.coderecon.wanderlkspringbackend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
}
