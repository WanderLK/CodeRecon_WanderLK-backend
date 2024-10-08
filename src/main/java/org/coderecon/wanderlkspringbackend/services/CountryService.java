package org.coderecon.wanderlkspringbackend.services;

import org.coderecon.wanderlkspringbackend.models.Country;
import org.coderecon.wanderlkspringbackend.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public ResponseEntity<Object> create(Country request) {
        Country country = countryRepository.save(request);
        return ResponseEntity.ok(country);
    }

    public ResponseEntity<Object> get(String id) {
        Optional<Country> country = countryRepository.findById(id);
        return country.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> delete(String id) {
        Optional<Country> country = countryRepository.findById(id);

        if (country.isPresent()) {
            countryRepository.delete(country.get());
            return ResponseEntity.ok("Country deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Country not found.");
        }
    }

    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(countryRepository.findAll());
    }

    public ResponseEntity<String> updateCountryField(String id, String fieldName, String newValue) {
        Optional<Country> optionalCountry = countryRepository.findById(id);

        if (optionalCountry.isPresent()) {
            Country country = optionalCountry.get();

            switch (fieldName) {
                case "name" -> country.setName(newValue);
                case "status" -> country.setStatus(newValue);
                default -> {
                    return ResponseEntity.badRequest().body("Invalid field name");
                }
            }

            countryRepository.save(country);
            return ResponseEntity.ok("Field updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Country not found");
        }
    }
}
