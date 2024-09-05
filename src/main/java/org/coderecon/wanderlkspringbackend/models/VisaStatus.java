package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;

@Data
public class VisaStatus {
    private String id;
    private String receivedEmbassy = "pending"; // passed, failed, pending
    private String reviewingApplication = "pending" ; // passed, failed, pending
    private String personalDetailsVerification = "pending" ; // passed, failed, pending
    private String imageVerification = "pending" ; // passed, failed, pending
    private String passportVerification = "pending"; // passed, failed, pending
    private String accommodationProofVerification = "pending" ; // passed, failed, pending
    private String returnPermitVerification = "pending" ; // passed, failed, pending
    private String visaRequestApproval = "pending" ; // passed, failed, pending
    private String visaProcessing = "pending" ; // passed, failed, pending
    private String visaProcessCompleted = "pending"; // passed, failed, pending
}
