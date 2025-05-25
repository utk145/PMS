package com.pms.patientservice.service;

import com.pms.patientservice.dto.PatientRequestVO;
import com.pms.patientservice.dto.PatientResponseVO;
import com.pms.patientservice.mapper.PatientMapper;
import com.pms.patientservice.model.Patient;
import com.pms.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository; // injecting the repository
    }

    public List<PatientResponseVO> getPatients() {
        return this.patientRepository.findAll().stream().map(PatientMapper::toPatientResponseDTO).toList();
    }

    public PatientResponseVO createPatient(PatientRequestVO patientRequestVO) {
        Patient patient = patientRepository.save((PatientMapper.toPatientEntityModelRequestDTO(patientRequestVO)));
        return PatientMapper.toPatientResponseDTO(patient);
    }
}
