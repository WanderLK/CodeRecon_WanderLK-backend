package org.coderecon.wanderlkspringbackend.repositories;

import org.coderecon.wanderlkspringbackend.models.Encryption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface EncryptionRepository extends MongoRepository<Encryption, String> {
}
