package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;

@Document(collection = "VisaDetail")
@Data
public class VisaDetails {
    @Id
    private String id;
    private String userID;

    private String dateOfApplied = LocalDate.now().toString();
    private String dateOfRequested ;
    private String dateOfIssued;
    
    // Country Details
    private String country;
    private String visaType;
    private String reason;
    private LocalDate requestDate;
    private String duration;

    // Personal Details
    private String fullName;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String countryOfBirth;
    private String currentNationality;
    private String gender;
    private String maritalStatus; // Married, Single

    // Photo Details
    private String photo;
    private PhotoValidation photoValidation;

    // Passport Details
    private String passportPhoto;
    private String passportNumber;
    private String passportIssueDate;
    private String passportExpiryDate;
    private String passportIssueAuthority;

    // Accommodation Details
    private String accommodationProof;

    // Return Permit
    private String returnPermit;

    // Visa Process Details
    private VisaStatus visaProcessStatus;

    private Map<String, Object> TravelHistory;
}
