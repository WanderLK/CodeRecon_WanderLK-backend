package org.coderecon.wanderlkspringbackend.repositories;

import org.coderecon.wanderlkspringbackend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
    Optional<User> findByEmail(String email);
    User findByEmailAndPassword(String email, String password);

    @Query("{ 'Email' : ?0 }")
    Optional<User> findUserByEmail(String email);

}
