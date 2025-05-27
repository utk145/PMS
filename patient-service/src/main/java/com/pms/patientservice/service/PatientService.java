package com.pms.patientservice.service;

import com.pms.patientservice.commons.PatientServiceCommonsConstants;
import com.pms.patientservice.dto.PatientRequestVO;
import com.pms.patientservice.dto.PatientResponseVO;
import com.pms.patientservice.exceptions.EmailAlreadyExistsException;
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
        if (patientRepository.existsByEmail(patientRequestVO.getEmail())) {
            throw new EmailAlreadyExistsException(PatientServiceCommonsConstants.EMAIL_ALREADY_EXISTS + PatientServiceCommonsConstants.COLON_WITH_TRAILING_SPACES + patientRequestVO.getEmail());
        }

        Patient patient = patientRepository.save((PatientMapper.toPatientEntityModelRequestDTO(patientRequestVO)));
        return PatientMapper.toPatientResponseDTO(patient);
    }
}
