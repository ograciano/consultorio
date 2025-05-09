package com.clinic.service;

import com.clinic.model.Appointment;
import com.clinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public Flux<Appointment> getAll() {
        return appointmentRepository.findByIsDeletedFalse();
    }

    public Mono<Appointment> getById(Long id) {
        return appointmentRepository.findById(id)
                .filter(app -> !Boolean.TRUE.equals(app.getIsDeleted()));
    }

    public Mono<Appointment> create(Appointment appointment) {
        // Reglas de validación:
        LocalDateTime startOfDay = appointment.getAppointmentTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return appointmentRepository.findByIsDeletedFalse()
                .collectList()
                .flatMap(existing -> {
                    if (hasOfficeConflict(existing, appointment)) {
                        return Mono.error(new IllegalArgumentException("Ya existe una cita en ese consultorio a esa hora."));
                    }
                    if (hasDoctorConflict(existing, appointment)) {
                        return Mono.error(new IllegalArgumentException("El doctor ya tiene una cita a esa hora."));
                    }
                    if (hasPatientConflict(existing, appointment)) {
                        return Mono.error(new IllegalArgumentException("El paciente ya tiene una cita en ese rango de tiempo."));
                    }
                    long totalAppointmentsToday = countDoctorAppointmentsToday(existing, appointment, startOfDay, endOfDay);
                    long beforeFive = countDoctorAppointmentsBeforeFive(existing, appointment, startOfDay, endOfDay);
                    boolean isAfterFive = appointment.getAppointmentTime().getHour() >= 17;

                    // regla final
                    if (totalAppointmentsToday >= 8 || (beforeFive >= 8 && !isAfterFive)) {
                        return Mono.error(new IllegalArgumentException("El doctor ya tiene el máximo de citas permitidas para este día."));
                    }
                    return appointmentRepository.save(appointment);
                });
    }

    public Mono<Appointment> update(Long id, Appointment updated) {
        return appointmentRepository.findById(id)
                .filter(existing -> !Boolean.TRUE.equals(existing.getIsDeleted()))
                .filter(existing -> existing.getAppointmentTime().isAfter(LocalDateTime.now()))
                .flatMap(existing -> {
                    existing.applyUpdates(updated);
                    return appointmentRepository.save(existing);
                });
    }

    public Mono<Void> delete(Long id) {
        return appointmentRepository.findById(id)
                .filter(existing -> !Boolean.TRUE.equals(existing.getIsDeleted()))
                .filter(existing -> existing.getAppointmentTime().isAfter(LocalDateTime.now()))
                .flatMap(existing -> {
                    existing.setIsDeleted(true);
                    return appointmentRepository.save(existing).then();
                });
    }

    private boolean hasOfficeConflict(java.util.List<Appointment> existing, Appointment app) {
        return existing.stream().anyMatch(a ->
                a.getOfficeId().equals(app.getOfficeId()) &&
                        a.getAppointmentTime().equals(app.getAppointmentTime()));
    }

    private boolean hasDoctorConflict(java.util.List<Appointment> existing, Appointment app) {
        return existing.stream().anyMatch(a ->
                a.getDoctorId().equals(app.getDoctorId()) &&
                        a.getAppointmentTime().equals(app.getAppointmentTime()));
    }

    private boolean hasPatientConflict(java.util.List<Appointment> existing, Appointment app) {
        return existing.stream().anyMatch(a ->
                a.getPatientName().equalsIgnoreCase(app.getPatientName()) &&
                        a.getAppointmentTime().toLocalDate().equals(app.getAppointmentTime().toLocalDate()) &&
                        Math.abs(a.getAppointmentTime().getHour() * 60 + a.getAppointmentTime().getMinute()
                                - app.getAppointmentTime().getHour() * 60 - app.getAppointmentTime().getMinute()) < 120);
    }

    private long countDoctorAppointmentsToday(java.util.List<Appointment> existing, Appointment app, LocalDateTime start, LocalDateTime end) {
        return existing.stream().filter(a ->
                a.getDoctorId().equals(app.getDoctorId()) &&
                        !a.getAppointmentTime().isBefore(start) &&
                        a.getAppointmentTime().isBefore(end)
        ).count();
    }

    private long countDoctorAppointmentsBeforeFive(List<Appointment> existing, Appointment app, LocalDateTime start, LocalDateTime end) {
        return existing.stream().filter(a ->
                a.getDoctorId().equals(app.getDoctorId()) &&
                        !a.getAppointmentTime().isBefore(start) &&
                        a.getAppointmentTime().isBefore(end) &&
                        a.getAppointmentTime().getHour() < 17
        ).count();
    }
}

