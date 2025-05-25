package com.pms.patientservice.mapper;

import com.pms.patientservice.dto.PatientRequestVO;
import com.pms.patientservice.dto.PatientResponseVO;
import com.pms.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseVO toPatientResponseDTO(Patient patient) {
        PatientResponseVO patientResponseVO = new PatientResponseVO();
        patientResponseVO.setId(patient.getId().toString());
        patientResponseVO.setName(patient.getName());
        patientResponseVO.setEmail(patient.getEmail());
        patientResponseVO.setAddress(patient.getAddress());
        patientResponseVO.setDateOfBirth(patient.getDateOfBirth().toString());
        return patientResponseVO;
    }

    public static Patient toPatientEntityModelRequestDTO(PatientRequestVO patientRequestVO) {
        Patient patient = new Patient();
        patient.setName(patientRequestVO.getName());
        patient.setEmail(patientRequestVO.getEmail());
        patient.setAddress(patientRequestVO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestVO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestVO.getRegisteredDate()));
        return patient;
    }
}
