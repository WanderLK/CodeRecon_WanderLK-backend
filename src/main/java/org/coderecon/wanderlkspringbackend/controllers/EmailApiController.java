package org.coderecon.wanderlkspringbackend.controllers;

import org.coderecon.wanderlkspringbackend.models.Email;
import org.coderecon.wanderlkspringbackend.services.EmailApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/email")
public class EmailApiController {

    private final WebClient webClient;

    @Autowired
    public EmailApiController(EmailApiService externalApiService, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:4040").build();
    }

    @PostMapping("/otp")
    public ResponseEntity<Object> otp(@RequestBody Email body) {
        Mono<String> response = webClient.post()
                .uri("mail/otp")
                .bodyValue(body)  // Pass the Email object as the request body
                .retrieve()
                .bodyToMono(String.class);

        return ResponseEntity.ok(response.block());
    }

    @PostMapping("/approve")
    public ResponseEntity<Object> approved(@RequestBody Email body) {
        Mono<String> response = webClient.post()
                .uri("mail/approved")
                .bodyValue(body)  // Pass the Email object as the request body
                .retrieve()
                .bodyToMono(String.class);

        return ResponseEntity.ok(response.block());
    }


    @PostMapping("/denied")
    public ResponseEntity<Object> denied(@RequestBody Email body) {
        Mono<String> response = webClient.post()
                .uri("mail/otp")
                .bodyValue(body)  // Pass the Email object as the request body
                .retrieve()
                .bodyToMono(String.class);

        return ResponseEntity.ok(response.block());
    }

    @PostMapping("/completed")
    public ResponseEntity<Object> completed(@RequestBody Email body) {
        Mono<String> response = webClient.post()
                .uri("mail/completed")
                .bodyValue(body)  // Pass the Email object as the request body
                .retrieve()
                .bodyToMono(String.class);

        return ResponseEntity.ok(response.block());
    }
}
