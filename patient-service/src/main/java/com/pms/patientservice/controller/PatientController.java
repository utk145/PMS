package com.pms.patientservice.controller;


import com.pms.patientservice.dto.PatientRequestVO;
import com.pms.patientservice.dto.PatientResponseVO;
import com.pms.patientservice.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping("/create-patient")
    public ResponseEntity<PatientResponseVO> createPatient(@Valid @RequestBody PatientRequestVO patientRequestVO) {
        return ResponseEntity.ok().body(this.patientService.createPatient(patientRequestVO));
    }

    @PutMapping("/update-patient/{patientId}")
    public ResponseEntity<PatientResponseVO> updatePatient(@PathVariable UUID patientId, @RequestBody PatientRequestVO patientRequestVO) {
        return ResponseEntity.ok().body(this.patientService.updatePatient(patientId, patientRequestVO));
    }

}
