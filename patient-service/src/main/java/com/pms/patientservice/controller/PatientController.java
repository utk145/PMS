package com.pms.patientservice.controller;


import com.pms.patientservice.dto.PatientRequestVO;
import com.pms.patientservice.dto.PatientResponseVO;
import com.pms.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pms.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patient", description = "APIs for managing patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/get-patients")
    @Operation(summary = "Get all patients", description = "Returns a list of all patients in the system")
    public ResponseEntity<List<PatientResponseVO>> getPatients() {
        return ResponseEntity.ok(this.patientService.getPatients());
    }

    @PostMapping("/create-patient")
    @Operation(summary = "Create a new patient", description = "Creates a new patient in the system")
    public ResponseEntity<PatientResponseVO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestVO patientRequestVO) {
        return ResponseEntity.ok().body(this.patientService.createPatient(patientRequestVO));
    }

    @PutMapping("/update-patient/{patientId}")
    @Operation(summary = "Update an existing patient", description = "Updates an existing patient in the system")
    public ResponseEntity<PatientResponseVO> updatePatient(@PathVariable UUID patientId, @Validated({Default.class}) @RequestBody PatientRequestVO patientRequestVO) {
        return ResponseEntity.ok().body(this.patientService.updatePatient(patientId, patientRequestVO));
    }

    @DeleteMapping("/delete-patient/{patientId}")
    @Operation(summary = "Delete an existing patient", description = "Deletes an existing patient from the system")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID patientId) {
        this.patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }

}
