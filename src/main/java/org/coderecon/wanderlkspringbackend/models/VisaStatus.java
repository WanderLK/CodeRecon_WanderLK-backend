package org.coderecon.wanderlkspringbackend.models;

import lombok.Data;

@Data
public class VisaStatus {
    private String receivedEmbassy; // passed, failed, pending
    private String reviewingApplication; // passed, failed, pending
    private String personalDetailsVerification; // passed, failed, pending
    private String imageVerification; // passed, failed, pending
    private String passportVerification; // passed, failed, pending
    private String accommodationProofVerification; // passed, failed, pending
    private String returnPermitVerification; // passed, failed, pending
    private String visaRequestApproval; // passed, failed, pending
    private String visaProcessing; // passed, failed, pending
    private String visaProcessCompleted; // passed, failed, pending
}
