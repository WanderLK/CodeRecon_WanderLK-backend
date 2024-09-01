package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
@Data
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
