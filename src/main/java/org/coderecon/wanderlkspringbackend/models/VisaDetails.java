package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "VisaDetail")
@Data
public class VisaDetails {
    @Id
    private String id;
    private String userID;

    // Country Details
    private String country;
    private String visaType;
    private String reason;

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
}
