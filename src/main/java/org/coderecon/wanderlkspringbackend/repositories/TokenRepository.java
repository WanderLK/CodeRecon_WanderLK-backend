package org.coderecon.wanderlkspringbackend.repositories;


import org.coderecon.wanderlkspringbackend.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, Integer> {

    List<Token> findAllAccessTokensByUser(String userId);

    Optional<Token> findByAccessToken(String token);

    Optional<Token> findByRefreshToken(String token);

    List<Token> findAllByUserId(String userId);


    List<Token> findAllByAccessTokenAndLoggedOut(String token, boolean loggedOut);
    List<Token> findAllByRefreshTokenAndLoggedOut(String token, boolean loggedOut);
}
