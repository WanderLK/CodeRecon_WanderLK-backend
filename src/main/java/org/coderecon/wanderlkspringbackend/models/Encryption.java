package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "encryptionKeys")
@Data
public class Encryption {
    @Id
    private String id;
    private String encryptionKey;
}
