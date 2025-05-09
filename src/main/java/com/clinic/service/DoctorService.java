package com.clinic.service;

import com.clinic.model.Doctor;
import com.clinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Flux<Doctor> getAll() {
        return doctorRepository.findAll()
                .filter(doctor -> !Boolean.TRUE.equals(doctor.getIsDeleted()));
    }

    public Flux<Doctor> getBySpeciality(String speciality) {
        return doctorRepository.findBySpecialty(speciality)
                .filter(doctor -> !Boolean.TRUE.equals(doctor.getIsDeleted()));
    }

    public Mono<Doctor> getById(Long id) {
        return doctorRepository.findById(id)
                .filter(doctor -> !Boolean.TRUE.equals(doctor.getIsDeleted()));
    }

    public Mono<Doctor> create(Doctor doctor) {
        return doctorRepository.save(doctor)
                .filter(savedDoctor -> !Boolean.TRUE.equals(savedDoctor.getIsDeleted()));
    }

    public Mono<Doctor> update(Long id, Doctor doctor) {
        return doctorRepository.findById(id)
                .flatMap(existingDoctor -> {
                    existingDoctor.applyUpdates(doctor);
                    return doctorRepository.save(existingDoctor);
                });
    }

    public Mono<Void> delete(Long id) {
        return doctorRepository.findById(id)
                .flatMap(existingDoctor -> {
                    existingDoctor.setIsDeleted(true);
                    return doctorRepository.save(existingDoctor).then();
                });
    }
}