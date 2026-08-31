package com.project.staragile;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicureRepository extends JpaRepository<Doctor, String> {

    List<Doctor> findByDoctorNameIgnoreCase(String doctorName);

}
