package com.clinic.controller;

import com.clinic.model.Office;
import com.clinic.service.OfficeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

class OfficeControllerTest {

    private OfficeService officeService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        officeService = Mockito.mock(OfficeService.class);
        OfficeController controller = new OfficeController(officeService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldCreateOffice() {
        Office office = new Office(null, 101, 1, false);
        Mockito.when(officeService.create(any())).thenReturn(Mono.just(office));

        webTestClient.post().uri("/offices")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(office)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.number").isEqualTo(101);
    }

    @Test
    void shouldReturnAllOffices() {
        Office o1 = new Office(1L, 101, 1, false);
        Office o2 = new Office(2L, 102, 2, false);
        Mockito.when(officeService.getAll()).thenReturn(Flux.just(o1, o2));

        webTestClient.get().uri("/offices")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Office.class)
                .hasSize(2);
    }

    @Test
    void shouldGetOfficeById() {
        Office office = new Office(1L, 101, 1, false);
        Mockito.when(officeService.getById(1L)).thenReturn(Mono.just(office));

        webTestClient.get().uri("/offices/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void shouldUpdateOffice() {
        Office updated = new Office(1L, 201, 3, false);
        Mockito.when(officeService.update(Mockito.eq(1L), any())).thenReturn(Mono.just(updated));

        webTestClient.put().uri("/offices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updated)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.number").isEqualTo(201);
    }

    @Test
    void shouldDeleteOffice() {
        Mockito.when(officeService.delete(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/offices/1")
                .exchange()
                .expectStatus().isOk();
    }
}

