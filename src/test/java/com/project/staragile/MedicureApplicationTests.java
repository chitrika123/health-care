package com.project.staragile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MedicureApplicationTests {

    @Autowired
    private MedicureService doctorService;

    @Test
    void contextLoads() {
        assertNotNull(doctorService);
    }

    @Test
    void testRegisterDoctor() {
        Doctor doctor = new Doctor(
                "TEST1001",
                "Test Doctor",
                "Cardiologist",
                "10 Years"
        );

        Doctor savedDoctor = doctorService.registerDoctor(doctor);

        assertNotNull(savedDoctor);
        assertEquals("TEST1001", savedDoctor.getDoctorRegistrationId());
        assertEquals("Test Doctor", savedDoctor.getDoctorName());
    }

    @Test
    void testSearchDoctor() {
        java.util.List<Doctor> doctors = doctorService.searchDoctor("Rajesh Kumar");

        assertFalse(doctors.isEmpty());
        assertEquals("Rajesh Kumar", doctors.get(0).getDoctorName());
    }

}
