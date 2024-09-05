package org.coderecon.wanderlkspringbackend.services;

import org.coderecon.wanderlkspringbackend.models.*;
import org.coderecon.wanderlkspringbackend.repositories.EncryptionRepository;
import org.coderecon.wanderlkspringbackend.repositories.UserRepository;
import org.coderecon.wanderlkspringbackend.repositories.VisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class VisaServices {
    @Autowired
    private VisaRepository visaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private EncryptionRepository encryptionRepository;

    private final WebClient webClient;

    @Autowired
    public VisaServices(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:4040/mail/").build();
    }

    public ResponseEntity<Object> create(VisaDetails request) throws Exception {
        User user = userRepository.findById(request.getUserID()).orElseThrow(() -> new RuntimeException("User not found"));
        Encryption encryptionKey = encryptionRepository.findById(user.getEncryptionKeyId()).orElseThrow(() -> new RuntimeException("Encryption key not found for user"));
        Key key = new SecretKeySpec(Base64.getDecoder().decode(encryptionKey.getEncryptionKey()), "AES");
        request.setPassportNumber(encryptionService.encrypt(request.getPassportNumber(), key));
        VisaDetails visaDetails = visaRepository.save(request);
        return ResponseEntity.ok(visaDetails);
    }

    public VisaDetails get(String id) throws Exception {
        VisaDetails visaDetail = visaRepository.findOneById(id);

        User user = userRepository.findById(visaDetail.getUserID()).orElseThrow(() -> new RuntimeException("User not found"));
        Encryption encryptionKey = encryptionRepository.findById(user.getEncryptionKeyId()).orElseThrow(() -> new RuntimeException("Encryption key not found for user"));
        Key key = new SecretKeySpec(Base64.getDecoder().decode(encryptionKey.getEncryptionKey()), "AES");

        visaDetail.setPassportNumber(encryptionService.decrypt(visaDetail.getPassportNumber(), key));
        return visaDetail;
    }

    public ResponseEntity<Object> delete(String id) {
        Optional<VisaDetails> visaDetails = visaRepository.findById(id);

        if (visaDetails.isPresent()) {
            visaRepository.delete(visaDetails.get());
            return ResponseEntity.ok("Visa deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Visa not found.");
        }
    }

    public ResponseEntity<String> updateVisaStatusField(String id, String fieldName, String newValue) {
        Optional<VisaDetails> optionalVisaDetails = visaRepository.findById(id);

        User user = userRepository.findById(optionalVisaDetails.get().getUserID()).get();

        VisaDetails visaDetails = optionalVisaDetails.get();
        VisaStatus visaStatus = visaDetails.getVisaProcessStatus();

        if (visaStatus == null) {
            visaStatus = new VisaStatus();
            visaDetails.setVisaProcessStatus(visaStatus);
        }

        switch (fieldName) {
            case "receivedEmbassy":
                visaStatus.setReceivedEmbassy(newValue);
                break;
            case "reviewingApplication":
                visaStatus.setReviewingApplication(newValue);
                break;
            case "personalDetailsVerification":
                visaStatus.setPersonalDetailsVerification(newValue);
                break;
            case "imageVerification":
                visaStatus.setImageVerification(newValue);
                break;
            case "passportVerification":
                visaStatus.setPassportVerification(newValue);
                break;
            case "accommodationProofVerification":
                visaStatus.setAccommodationProofVerification(newValue);
                break;
            case "returnPermitVerification":
                visaStatus.setReturnPermitVerification(newValue);
                break;
            case "visaRequestApproval":

                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setFirstname(user.getFirstName());
                emailRequest.setEmail(user.getEmail());

                if(Objects.equals(newValue, "passed")) {
                    Mono<String> response = webClient.post()
                            .uri("approved")
                            .bodyValue(emailRequest)
                            .retrieve()
                            .bodyToMono(String.class);

                    System.out.println(response.block());
                }
                else{
                    Mono<String> response = webClient.post()
                            .uri("denied")
                            .bodyValue(emailRequest)
                            .retrieve()
                            .bodyToMono(String.class);

                    System.out.println(response.block());
                }
                visaStatus.setVisaRequestApproval(newValue);
                break;

            case "visaProcessing":
                visaStatus.setVisaProcessing(newValue);
                break;
            case "visaProcessCompleted":
                visaStatus.setVisaProcessCompleted(newValue);
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid field name: " + fieldName);
        }

        visaRepository.save(visaDetails);
        return ResponseEntity.ok("Visa status '" + fieldName + "' updated successfully.");
    }
}
