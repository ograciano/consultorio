package com.clinic.controller;

import com.clinic.model.Doctor;
import com.clinic.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

class DoctorControllerTest {

    private DoctorService doctorService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        doctorService = Mockito.mock(DoctorService.class);
        DoctorController controller = new DoctorController(doctorService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldCreateDoctor() {
        Doctor doctor = new Doctor(null, "John", "Doe", "Smith", "Cardiology", false);
        Mockito.when(doctorService.create(any())).thenReturn(Mono.just(doctor));

        webTestClient.post().uri("/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(doctor)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("John");
    }

    @Test
    void shouldReturnAllDoctors() {
        Doctor doc1 = new Doctor(1L, "A", "B", "C", "X", false);
        Doctor doc2 = new Doctor(2L, "D", "E", "F", "Y", false);

        Mockito.when(doctorService.getAll()).thenReturn(Flux.just(doc1, doc2));

        webTestClient.get().uri("/doctors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Doctor.class)
                .hasSize(2);
    }

    @Test
    void shouldGetDoctorById() {
        Doctor doc = new Doctor(1L, "A", "B", "C", "X", false);
        Mockito.when(doctorService.getById(1L)).thenReturn(Mono.just(doc));

        webTestClient.get().uri("/doctors/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void shouldUpdateDoctor() {
        Doctor updated = new Doctor(1L, "Ana", "Perez", "Lopez", "Dermatology", false);
        Mockito.when(doctorService.update(Mockito.eq(1L), any())).thenReturn(Mono.just(updated));

        webTestClient.put().uri("/doctors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updated)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.specialty").isEqualTo("Dermatology");
    }

    @Test
    void shouldDeleteDoctor() {
        Mockito.when(doctorService.delete(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/doctors/1")
                .exchange()
                .expectStatus().isOk();
    }
}

