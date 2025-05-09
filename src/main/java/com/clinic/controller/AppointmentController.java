package com.clinic.controller;

import com.clinic.model.Appointment;
import com.clinic.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Validated
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public Flux<Appointment> getAll() {
        return appointmentService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Appointment> getById(@PathVariable Long id) {
        return appointmentService.getById(id);
    }

    @PostMapping
    public Mono<Appointment> create(@Valid @RequestBody Appointment appointment) {
        return appointmentService.create(appointment);
    }

    @PutMapping("/{id}")
    public Mono<Appointment> update(@PathVariable Long id, @RequestBody Appointment appointment) {
        return appointmentService.update(id, appointment);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return appointmentService.delete(id);
    }

    @GetMapping("/doctor/{doctorId}")
    public Flux<Appointment> getByDoctor(@PathVariable Long doctorId) {
        return appointmentService.getAll()
                .filter(app -> app.getDoctorId().equals(doctorId));
    }

    @GetMapping("/office/{officeId}")
    public Flux<Appointment> getByOffice(@PathVariable Long officeId) {
        return appointmentService.getAll()
                .filter(app -> app.getOfficeId().equals(officeId));
    }

    @GetMapping("/date/{date}")
    public Flux<Appointment> getByDate(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        LocalDateTime start = parsedDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return appointmentService.getAll()
                .filter(app -> !app.getAppointmentTime().isBefore(start) && app.getAppointmentTime().isBefore(end));
    }
}
