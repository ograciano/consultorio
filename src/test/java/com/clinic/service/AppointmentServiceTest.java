package com.clinic.service;

import com.clinic.model.Appointment;
import com.clinic.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class AppointmentServiceTest {

    private AppointmentRepository appointmentRepository;
    private AppointmentService appointmentService;

    @BeforeEach
    void setup() {
        appointmentRepository = Mockito.mock(AppointmentRepository.class);
        appointmentService = new AppointmentService(appointmentRepository);
    }

    @Test
    void shouldSaveAppointmentWhenNoConflict() {
        Appointment appointment = new Appointment(null, 1L, 1L, "John Doe", LocalDateTime.now().plusDays(1), Appointment.AppointmentStatus.PENDING, false);

        Mockito.when(appointmentRepository.findByIsDeletedFalse())
                .thenReturn(Flux.empty());

        Mockito.when(appointmentRepository.save(any())).thenReturn(Mono.just(appointment));

        StepVerifier.create(appointmentService.create(appointment))
                .expectNextMatches(saved -> saved.getPatientName().equals("John Doe"))
                .verifyComplete();
    }

    @Test
    void shouldRejectIfDoctorHas8Appointments() {
        Appointment incoming = new Appointment(null, 1L, 1L, "Jane Roe", LocalDateTime.of(2025, 5, 10, 10, 0), Appointment.AppointmentStatus.PENDING, false);
        List<Appointment> existing = List.of(
                new Appointment(1L, 1L, 1L, "A", LocalDateTime.of(2025, 5, 10, 8, 0), Appointment.AppointmentStatus.PENDING, false),
                new Appointment(2L, 1L, 1L, "B", LocalDateTime.of(2025, 5, 10, 8, 30), Appointment.AppointmentStatus.PENDING, false),
                new Appointment(3L, 1L, 1L, "C", LocalDateTime.of(2025, 5, 10, 9, 0), Appointment.AppointmentStatus.PENDING, false),
                new Appointment(4L, 1L, 1L, "D", LocalDateTime.of(2025, 5, 10, 9, 30), Appointment.AppointmentStatus.PENDING, false),
                new Appointment(5L, 1L, 1L, "E", LocalDateTime.of(2025, 5, 10, 10, 0), Appointment.AppointmentStatus.PENDING, false),
                new Appointment(6L, 1L, 1L, "F", LocalDateTime.of(2025, 5, 10, 10, 30), Appointment.AppointmentStatus.PENDING, false),
                new Appointment(7L, 1L, 1L, "G", LocalDateTime.of(2025, 5, 10, 11, 0), Appointment.AppointmentStatus.PENDING, false),
                new Appointment(8L, 1L, 1L, "H", LocalDateTime.of(2025, 5, 10, 11, 30), Appointment.AppointmentStatus.PENDING, false)
        );

        Mockito.when(appointmentRepository.findByIsDeletedFalse())
                .thenReturn(Flux.fromIterable(existing));

        StepVerifier.create(appointmentService.create(incoming))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Ya existe una cita en ese consultorio a esa hora"))
                .verify();
    }

    @Test
    void shouldRejectIfOfficeConflictExists() {
        Appointment incoming = new Appointment(null, 1L, 2L, "Pedro", LocalDateTime.of(2025, 5, 10, 9, 0), Appointment.AppointmentStatus.PENDING, false);
        Appointment conflict = new Appointment(1L, 2L, 2L, "Otro", LocalDateTime.of(2025, 5, 10, 9, 0), Appointment.AppointmentStatus.PENDING, false);

        Mockito.when(appointmentRepository.findByIsDeletedFalse()).thenReturn(Flux.just(conflict));

        StepVerifier.create(appointmentService.create(incoming))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException && e.getMessage().contains("consultorio"))
                .verify();
    }

    @Test
    void shouldRejectIfDoctorConflictExists() {
        Appointment incoming = new Appointment(null, 1L, 1L, "Luis", LocalDateTime.of(2025, 5, 10, 10, 0), Appointment.AppointmentStatus.PENDING, false);
        Appointment conflict = new Appointment(2L, 1L, 2L, "Otro", LocalDateTime.of(2025, 5, 10, 10, 0), Appointment.AppointmentStatus.PENDING, false);

        Mockito.when(appointmentRepository.findByIsDeletedFalse()).thenReturn(Flux.just(conflict));

        StepVerifier.create(appointmentService.create(incoming))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException && e.getMessage().contains("doctor"))
                .verify();
    }

    @Test
    void shouldRejectIfPatientHasAnotherAppointmentClose() {
        Appointment incoming = new Appointment(null, 3L, 3L, "Ana", LocalDateTime.of(2025, 5, 10, 10, 0), Appointment.AppointmentStatus.PENDING, false);
        Appointment previous = new Appointment(3L, 4L, 4L, "Ana", LocalDateTime.of(2025, 5, 10, 9, 0), Appointment.AppointmentStatus.PENDING, false);

        Mockito.when(appointmentRepository.findByIsDeletedFalse()).thenReturn(Flux.just(previous));

        StepVerifier.create(appointmentService.create(incoming))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException && e.getMessage().contains("rango de tiempo"))
                .verify();
    }
}
