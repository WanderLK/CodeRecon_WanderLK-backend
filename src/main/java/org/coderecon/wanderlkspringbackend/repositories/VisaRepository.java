package org.coderecon.wanderlkspringbackend.repositories;

import org.coderecon.wanderlkspringbackend.models.VisaDetails;
import org.coderecon.wanderlkspringbackend.models.VisaStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface VisaRepository extends MongoRepository<VisaDetails, VisaStatus> {

    Optional<VisaDetails> findById(String Id);

    @Query("{ 'id' :  ?0 }")
    VisaDetails findOneById(String id);
}
