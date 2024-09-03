package org.coderecon.wanderlkspringbackend.repositories;

import org.coderecon.wanderlkspringbackend.models.VisaDetails;
import org.coderecon.wanderlkspringbackend.models.VisaStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface VisaRepository extends MongoRepository<VisaDetails, VisaStatus> {

    Optional<VisaDetails> findById(String Id);

}
