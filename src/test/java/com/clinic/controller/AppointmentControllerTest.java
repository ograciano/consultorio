package com.clinic.controller;

import com.clinic.model.Appointment;
import com.clinic.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;

class AppointmentControllerTest {

    private AppointmentService appointmentService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        appointmentService = Mockito.mock(AppointmentService.class);
        AppointmentController controller = new AppointmentController(appointmentService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldCreateAppointment() {
        Appointment appt = new Appointment(null, 1L, 1L, "Alice", LocalDateTime.now().plusDays(1), Appointment.AppointmentStatus.PENDING, false);
        Mockito.when(appointmentService.create(any())).thenReturn(Mono.just(appt));

        webTestClient.post().uri("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(appt)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.patientName").isEqualTo("Alice");
    }

    @Test
    void shouldReturnAllAppointments() {
        Appointment a1 = new Appointment(1L, 1L, 1L, "John", LocalDateTime.now().plusDays(1), Appointment.AppointmentStatus.PENDING, false);
        Appointment a2 = new Appointment(2L, 1L, 1L, "Jane", LocalDateTime.now().plusDays(2), Appointment.AppointmentStatus.PENDING, false);

        Mockito.when(appointmentService.getAll()).thenReturn(Flux.just(a1, a2));

        webTestClient.get().uri("/appointments")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Appointment.class)
                .hasSize(2);
    }

    @Test
    void shouldGetAppointmentById() {
        Appointment appt = new Appointment(1L, 1L, 1L, "John", LocalDateTime.now().plusDays(1), Appointment.AppointmentStatus.PENDING, false);
        Mockito.when(appointmentService.getById(1L)).thenReturn(Mono.just(appt));

        webTestClient.get().uri("/appointments/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void shouldUpdateAppointment() {
        Appointment updated = new Appointment(1L, 2L, 2L, "Carlos", LocalDateTime.now().plusDays(3), Appointment.AppointmentStatus.CONFIRMED, false);
        Mockito.when(appointmentService.update(Mockito.eq(1L), any())).thenReturn(Mono.just(updated));

        webTestClient.put().uri("/appointments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updated)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("CONFIRMED");
    }

    @Test
    void shouldDeleteAppointment() {
        Mockito.when(appointmentService.delete(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/appointments/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldFilterByDoctor() {
        Appointment appt = new Appointment(1L, 99L, 1L, "Paciente", LocalDateTime.now().plusDays(1), Appointment.AppointmentStatus.PENDING, false);
        Mockito.when(appointmentService.getAll()).thenReturn(Flux.just(appt));

        webTestClient.get().uri("/appointments/doctor/99")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Appointment.class)
                .hasSize(1);
    }

    @Test
    void shouldFilterByOffice() {
        Appointment appt = new Appointment(1L, 1L, 88L, "Paciente", LocalDateTime.now().plusDays(1), Appointment.AppointmentStatus.PENDING, false);
        Mockito.when(appointmentService.getAll()).thenReturn(Flux.just(appt));

        webTestClient.get().uri("/appointments/office/88")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Appointment.class)
                .hasSize(1);
    }

    @Test
    void shouldFilterByDate() {
        LocalDateTime now = LocalDateTime.of(2025, 5, 10, 10, 0);
        Appointment appt = new Appointment(1L, 1L, 1L, "Paciente", now, Appointment.AppointmentStatus.PENDING, false);
        Mockito.when(appointmentService.getAll()).thenReturn(Flux.just(appt));

        webTestClient.get().uri("/appointments/date/2025-05-10")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Appointment.class)
                .hasSize(1);
    }



    @Test
    void shouldAllowNinthAppointmentAfterFive() {
        Appointment incoming = new Appointment(null, 1L, 1L, "Carlos", LocalDateTime.of(2025, 5, 10, 17, 30), Appointment.AppointmentStatus.PENDING, false);

        List<Appointment> eightAppointments = IntStream.range(0, 8)
                .mapToObj(i -> new Appointment((long) i, 1L, 1L, "Paciente " + i, LocalDateTime.of(2025, 5, 10, 8, 0), Appointment.AppointmentStatus.PENDING, false))
                .collect(Collectors.toList());

        Mockito.when(appointmentService.getAll()).thenReturn(Flux.fromIterable(eightAppointments));
        Mockito.when(appointmentService.create(any())).thenReturn(Mono.just(incoming));

        webTestClient.post().uri("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(incoming)
                .exchange()
                .expectStatus().isOk();
    }
}
