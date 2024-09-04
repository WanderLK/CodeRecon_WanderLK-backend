package org.coderecon.wanderlkspringbackend.Configuration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.coderecon.wanderlkspringbackend.dto.AuthenticationResponse;
import org.coderecon.wanderlkspringbackend.models.Encryption;
import org.coderecon.wanderlkspringbackend.models.Token;
import org.coderecon.wanderlkspringbackend.models.User;
import org.coderecon.wanderlkspringbackend.repositories.EncryptionRepository;
import org.coderecon.wanderlkspringbackend.repositories.TokenRepository;
import org.coderecon.wanderlkspringbackend.repositories.UserRepository;
import org.coderecon.wanderlkspringbackend.services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Key;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;



    @Autowired
    private EncryptionRepository encryptionRepository;

    @Autowired
    private EncryptionService encryptionService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, TokenRepository tokenRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    // Handles user registration
    public AuthenticationResponse register(User request) throws Exception {
        // Generate a unique key for the user
        Key key = encryptionService.generateKey();

        // Save the encryption key
        Encryption encryptionKey = new Encryption();
        encryptionKey.setEncryptionKey(Base64.getEncoder().encodeToString(key.getEncoded()));
        encryptionRepository.save(encryptionKey);


//        String encryptedData = encryptionService.encrypt(data, key);

        System.out.println("Inside register " + request.getEmail());

        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthenticationResponse(null, null, "User already exists");
        }

        // Create and save new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEncryptionKeyId(encryptionKey.getId());

        // Validate and set user role
        if (request.getRole() == null) {
            request.setRole("TOURIST");
//            return new AuthenticationResponse(null, null, "Role is required");
        }

        // Check if the role is valid
//        if (isValidRole(request.getRole())) {
//            user.setRole(request.getRole());
//            if (requiresCafeId(request.getRole())) {
//                user.setCafeId(request.getCafeId());
//            }
//        } else {
//            return new AuthenticationResponse(null, null, "Invalid role");
//        }

        // Save user and generate tokens
        user = userRepository.save(user);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Save user tokens
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User registration was successful");
    }

    // Helper method to validate role ---------------------- TO DO




    // Saves tokens for the user
    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    // Handles user authentication
    public AuthenticationResponse authenticate(User request) {
        System.out.println("Inside authenticate " + request.getEmail() + " " + request.getPassword());

        // Authenticate the user
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );

        User backUser = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        String incomingPassword = passwordEncoder.encode(request.getPassword());
        System.out.println("Incoming password: " + incomingPassword);
        System.out.println("Back password: " + backUser.getPassword());

        if (passwordEncoder.matches(request.getPassword(), backUser.getPassword())) {
            System.out.println("Password matches");
        } else {
            System.out.println("Password does not match");
            return new AuthenticationResponse(null, null, "User login failed. Password does not match");
        }

        // Retrieve user and generate tokens
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User: " + user.getEmail() + " UserID: " + user.getId());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Revoke old tokens and save new ones
//        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User login was successful");
    }

    // Revokes all tokens for a user
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllByUserId(user.getId());
        System.out.println("Valid tokens: " + validTokens.size());
        if (!validTokens.isEmpty()) {
            validTokens.forEach(t -> t.setLoggedOut(true));
            tokenRepository.saveAll(validTokens);
        }
    }

    private void revokeAllTokenByAcessToken(String token) {
        List<Token> validToken = tokenRepository.findAllByAccessTokenAndLoggedOut(token, true);
        if (!validToken.isEmpty()) {
            validToken.forEach(t -> t.setLoggedOut(true));
            tokenRepository.saveAll(validToken);
        }
    }

    private void revokeAllTokenByRefreshToken(String token) {
        List<Token> validToken = tokenRepository.findAllByRefreshTokenAndLoggedOut(token, true);
        if (!validToken.isEmpty()) {
            validToken.forEach(t -> t.setLoggedOut(true));
            tokenRepository.saveAll(validToken);
        }
    }

    // Handles token refresh
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // Extract the token from the authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // Extract username from token
        String email = jwtService.extractUsername(token);

        // Check if the user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user found"));

        // Validate the refresh token
        if (jwtService.isValidRefreshToken(token, user)) {
            // Generate new tokens
            String accessToken = jwtService.generateAccessToken(user);
//            String refreshToken = jwtService.generateRefreshToken(user);

            // Revoke old tokens and save new ones
//            revokeAllTokenByUser(user);
//            revokeAllTokenByAcessToken(token);
            revokeAllTokenByRefreshToken(token);
            saveUserToken(token, accessToken, user);

            return new ResponseEntity<>(new AuthenticationResponse(accessToken, token, "New token generated"), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        // Extract the token from the authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // Extract username from token
        String email = jwtService.extractUsername(token);

        // Check if the user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user found"));

        // Revoke all tokens
        revokeAllTokenByRefreshToken(token);
        System.out.println("Done");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}