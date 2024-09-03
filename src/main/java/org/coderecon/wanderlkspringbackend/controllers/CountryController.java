package org.coderecon.wanderlkspringbackend.controllers;


import org.coderecon.wanderlkspringbackend.models.Country;
import org.coderecon.wanderlkspringbackend.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @PostMapping("/create")
    public ResponseEntity<Object> addCountry(@RequestBody Country body) {
        return countryService.create(body);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> viewCountry(@PathVariable String id) {
        return countryService.get(id);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> viewAllCountries(@PathVariable String id) {
        return countryService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable String id) {
        return countryService.delete(id);
    }

    @PatchMapping("/update-status/{id}")
    public ResponseEntity<String> updateCountryField(@PathVariable String id,
                                                    @RequestParam String fieldName,
                                                    @RequestParam String newValue) {
        return countryService.updateCountryField(id, fieldName, newValue);
    }
}
