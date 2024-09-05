package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Email {
    @Id
    private String firstName;
    private String email;
}
