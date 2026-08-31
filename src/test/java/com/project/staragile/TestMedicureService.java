package com.project.staragile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestMedicureService {

    @Autowired
    private MedicureService doctorService;

    @Test
    void testUpdateDoctor() {

        Doctor updatedDoctor = new Doctor(
                "MED1001",
                "Rajesh Kumar Updated",
                "Cardiac Surgeon",
                "16 Years"
        );

        Doctor result = doctorService.updateDoctor(
                "MED1001",
                updatedDoctor
        );

        assertNotNull(result);
        assertEquals(
                "Rajesh Kumar Updated",
                result.getDoctorName()
        );
        assertEquals(
                "Cardiac Surgeon",
                result.getDoctorSpeciality()
        );
    }

    @Test
    void testDeleteDoctor() {

        Doctor doctor = new Doctor(
                "TEST1002",
                "Delete Test Doctor",
                "General Surgeon",
                "5 Years"
        );

        doctorService.registerDoctor(doctor);

        String result = doctorService.deleteDoctor("TEST1002");

        assertEquals("Doctor deleted successfully", result);
    }

}
