package org.coderecon.wanderlkspringbackend.services;

import org.coderecon.wanderlkspringbackend.models.VisaDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.coderecon.wanderlkspringbackend.repositories.VisaDetailsRepository;

@Service
public class VisaDetailsService {
    private final VisaDetailsRepository visaDetailsRepository;

    @Autowired
    public VisaDetailsService(VisaDetailsRepository visaDetailsRepository) {
        this.visaDetailsRepository = visaDetailsRepository;
    }

    public String addNewVisaDetails(VisaDetails visaDetails) {
        visaDetailsRepository.save(visaDetails);
        return "Visa Details added successfully";
    }
}
