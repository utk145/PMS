package com.pms.patientservice.mapper;

import com.pms.patientservice.dto.PatientResponseVO;
import com.pms.patientservice.model.Patient;

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
}
