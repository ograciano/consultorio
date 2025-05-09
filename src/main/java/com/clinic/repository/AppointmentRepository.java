package com.clinic.repository;

import com.clinic.model.Appointment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface AppointmentRepository extends ReactiveCrudRepository<Appointment, Long> {

    Flux<Appointment> findByDoctorIdAndIsDeletedFalse(Long doctorId);

    Flux<Appointment> findByOfficeIdAndIsDeletedFalse(Long officeId);

    Flux<Appointment> findByAppointmentTimeBetweenAndIsDeletedFalse(LocalDateTime start, LocalDateTime end);

    Flux<Appointment> findByDoctorIdAndAppointmentTimeBetweenAndIsDeletedFalse(Long doctorId, LocalDateTime start, LocalDateTime end);

    Flux<Appointment> findByPatientNameAndAppointmentTimeBetweenAndIsDeletedFalse(String patientName, LocalDateTime start, LocalDateTime end);

    Flux<Appointment> findByIsDeletedFalse();
}
