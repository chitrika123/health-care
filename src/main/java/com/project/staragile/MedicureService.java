package com.project.staragile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicureService {

    @Autowired
    private MedicureRepository doctorRepository;

    public Doctor registerDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(String doctorRegNo, Doctor doctor) {
        Doctor existingDoctor = doctorRepository.findById(doctorRegNo)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        existingDoctor.setDoctorName(doctor.getDoctorName());
        existingDoctor.setDoctorSpeciality(doctor.getDoctorSpeciality());
        existingDoctor.setDoctorExperience(doctor.getDoctorExperience());

        return doctorRepository.save(existingDoctor);
    }

    public List<Doctor> searchDoctor(String doctorName) {
        return doctorRepository.findByDoctorNameIgnoreCase(doctorName);
    }

    public String deleteDoctor(String doctorRegNo) {
        if (!doctorRepository.existsById(doctorRegNo)) {
            throw new RuntimeException("Doctor not found");
        }

        doctorRepository.deleteById(doctorRegNo);
        return "Doctor deleted successfully";
    }

}
