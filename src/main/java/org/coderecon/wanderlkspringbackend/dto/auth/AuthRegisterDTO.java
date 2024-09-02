package org.coderecon.wanderlkspringbackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterDTO {
    @NotNull(message = "first name is required")
    private String first_name;
    @NotNull(message = "last name is required")
    private String last_name;
    @NotNull(message = "email is required")
    @Email(message = "please provide a valid email")
    private String email;
    @NotNull(message = "password is required")
    private String password;
}
