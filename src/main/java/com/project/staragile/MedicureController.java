package com.project.staragile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicureController {

    @Autowired
    private MedicureService doctorService;

    @PostMapping("/registerDoctor")
    public Doctor registerDoctor(@RequestBody Doctor doctor) {
        return doctorService.registerDoctor(doctor);
    }

    @PutMapping("/updateDoctor/{doctorRegNo}")
    public Doctor updateDoctor(
            @PathVariable String doctorRegNo,
            @RequestBody Doctor doctor) {

        return doctorService.updateDoctor(doctorRegNo, doctor);
    }

    @GetMapping("/searchDoctor/{doctorName}")
    public List<Doctor> searchDoctor(@PathVariable String doctorName) {
        return doctorService.searchDoctor(doctorName);
    }

    @DeleteMapping("/deletePolicy/{doctorRegNo}")
    public String deleteDoctor(@PathVariable String doctorRegNo) {
        return doctorService.deleteDoctor(doctorRegNo);
    }

}
