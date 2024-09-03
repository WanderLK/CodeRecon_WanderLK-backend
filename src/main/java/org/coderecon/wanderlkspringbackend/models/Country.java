package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Country")
@Data
public class Country {
    @Id
    private String id;
    private String name;
    private String status; // Allowed - 1, Deleted - 2, Banned - 3
}
