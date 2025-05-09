package com.clinic.controller;

import com.clinic.model.Doctor;
import com.clinic.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Validated
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public Flux<Doctor> getAll() {
        return doctorService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Doctor> getById(@PathVariable Long id) {
        return doctorService.getById(id);
    }

    @GetMapping("/speciality/{speciality}")
    public Flux<Doctor> getBySpeciality(@PathVariable String speciality) {
        return doctorService.getBySpeciality(speciality);
    }

    @PostMapping
    public Mono<Doctor> create(@RequestBody @Valid Doctor doctor) {
        return doctorService.create(doctor);
    }

    @PutMapping("/{id}")
    public Mono<Doctor> update(@PathVariable Long id, @RequestBody Doctor doctor) {
        return doctorService.update(id, doctor);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return doctorService.delete(id);
    }
}