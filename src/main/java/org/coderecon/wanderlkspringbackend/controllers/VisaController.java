package org.coderecon.wanderlkspringbackend.controllers;


import org.coderecon.wanderlkspringbackend.models.VisaDetails;
import org.coderecon.wanderlkspringbackend.services.VisaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/visa")
public class VisaController {

    @Autowired
    private VisaServices visaServices;

    @PostMapping("/create")
    public ResponseEntity<Object> createVisa(@RequestBody VisaDetails body) {
        return visaServices.create(body);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getVisa(@PathVariable String id) {
        return visaServices.get(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteVisa(@PathVariable String id) {
        return visaServices.delete(id);
    }

    @PatchMapping("/update-status/{id}")
    public ResponseEntity<String> updateVisaStatusField(@PathVariable String id,
                                                        @RequestParam String fieldName,
                                                        @RequestParam String newValue) {
        return visaServices.updateVisaStatusField(id, fieldName, newValue);
    }
}
