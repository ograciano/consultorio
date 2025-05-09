package com.clinic.repository;

import com.clinic.model.Doctor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DoctorRepository extends ReactiveCrudRepository<Doctor, Long> {
    Flux<Doctor> findBySpecialty(String speciality);
}