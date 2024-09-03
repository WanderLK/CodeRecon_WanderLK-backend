package org.coderecon.wanderlkspringbackend.services;

import org.coderecon.wanderlkspringbackend.models.VisaDetails;
import org.coderecon.wanderlkspringbackend.models.VisaStatus;
import org.coderecon.wanderlkspringbackend.repositories.VisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class VisaServices {
    @Autowired
    private VisaRepository visaRepository;

    public ResponseEntity<Object> create(VisaDetails request) {
            VisaDetails visaDetails = visaRepository.save(request);
            return ResponseEntity.ok(visaDetails);
    }

    public ResponseEntity<Object> get(String id) {
        Optional<VisaDetails> visaDetails = visaRepository.findById(id);
        return visaDetails.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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

        // Logging to check if VisaDetails object is present
        System.out.println(optionalVisaDetails.isPresent());

        if (optionalVisaDetails.isPresent()) {
            VisaDetails visaDetails = optionalVisaDetails.get();

            // Logging the current state of the VisaDetails object
            System.out.println(visaDetails);

            // Retrieve the VisaStatus object
            VisaStatus visaStatus = visaDetails.getVisaProcessStatus();

            // Handle case where VisaStatus might be null
            if (visaStatus == null) {
                visaStatus = new VisaStatus();
                visaDetails.setVisaProcessStatus(visaStatus);
            }

            // Update the appropriate field in VisaStatus
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

            // Save the updated VisaDetails entity
            visaRepository.save(visaDetails);
            return ResponseEntity.ok("Visa status '" + fieldName + "' updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Visa not found.");
        }
    }
}
