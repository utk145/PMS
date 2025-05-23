package com.pms.patientservice.controller;


import com.pms.patientservice.dto.PatientResponseVO;
import com.pms.patientservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/get-patients")
    public ResponseEntity<List<PatientResponseVO>> getPatients() {
        return ResponseEntity.ok(this.patientService.getPatients());
    }
}
