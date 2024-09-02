package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Token")
@Data
public class Token {
    @Id
    private String id;
    private String accessToken;
    private String refreshToken;
    private boolean loggedOut;
    private User user;

}
