package com.project.staragile;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MedicureApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicureApplication.class, args);
    }

    @Bean
    CommandLineRunner loadData(MedicureRepository repository) {
        return args -> {
            repository.save(new Doctor(
                    "MED1001",
                    "Rajesh Kumar",
                    "Cardiologist",
                    "15 Years"
            ));

            repository.save(new Doctor(
                    "MED1002",
                    "Anita Sharma",
                    "Neurologist",
                    "12 Years"
            ));

            repository.save(new Doctor(
                    "MED1003",
                    "Michael Brown",
                    "Nephrologist",
                    "18 Years"
            ));

            repository.save(new Doctor(
                    "MED1004",
                    "Sarah Wilson",
                    "Liver Transplant Specialist",
                    "20 Years"
            ));
        };
    }
}
