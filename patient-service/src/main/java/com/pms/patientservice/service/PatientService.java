package com.pms.patientservice.service;

import static com.pms.patientservice.commons.PatientServiceCommonsConstants.EMAIL_UPDATE_NOT_ALLOWED_HERE;
import static com.pms.patientservice.commons.PatientServiceCommonsConstants.EMAIL_ALREADY_EXISTS;
import static com.pms.patientservice.commons.PatientServiceCommonsConstants.COLON_WITH_TRAILING_SPACES;

import com.pms.patientservice.dto.PatientRequestVO;
import com.pms.patientservice.dto.PatientResponseVO;
import com.pms.patientservice.exceptions.EmailAlreadyExistsException;
import com.pms.patientservice.exceptions.PatientNotFoundException;
import com.pms.patientservice.mapper.PatientMapper;
import com.pms.patientservice.model.Patient;
import com.pms.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
            throw new EmailAlreadyExistsException(EMAIL_ALREADY_EXISTS + COLON_WITH_TRAILING_SPACES + patientRequestVO.getEmail());
        }

        Patient patient = patientRepository.save((PatientMapper.toPatientEntityModelRequestDTO(patientRequestVO)));
        return PatientMapper.toPatientResponseDTO(patient);
    }

    public PatientResponseVO updatePatient(UUID patientId, PatientRequestVO patientRequestVO) {
        Patient patient = this.patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException("Patient mot found with ID: " + patientId));
//        #TODO: uncomment and test this when connected to actual database as currently the data stored in in-memory db h2 is different from what is being retrieved
//        if (!patient.getEmail().trim().equalsIgnoreCase(patientRequestVO.getEmail().trim())) {
//            throw new EmailAlreadyExistsException(EMAIL_UPDATE_NOT_ALLOWED_HERE);
//        }
//        System.out.println("DB email: '" + patient.getEmail() + "'");
//        System.out.println("Request email: '" + patientRequestVO.getEmail() + "'");

        patient.setName(patientRequestVO.getName());
        patient.setAddress(patientRequestVO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestVO.getDateOfBirth()));
        patient.setEmail(patientRequestVO.getEmail());
        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toPatientResponseDTO(updatedPatient);
    }
}
