package com.clinic.service;

import com.clinic.model.Office;
import com.clinic.repository.OfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

class OfficeServiceTest {

    private OfficeRepository officeRepository;
    private OfficeService officeService;

    @BeforeEach
    void setup() {
        officeRepository = Mockito.mock(OfficeRepository.class);
        officeService = new OfficeService(officeRepository);
    }

    @Test
    void shouldCreateOffice() {
        Office office = new Office(null, 101, 1, false);
        Mockito.when(officeRepository.save(any())).thenReturn(Mono.just(office));

        StepVerifier.create(officeService.create(office))
                .expectNextMatches(o -> o.getNumber().equals(101))
                .verifyComplete();
    }

    @Test
    void shouldUpdateOffice() {
        Office original = new Office(1L, 200, 2, false);
        Office updated = new Office(null, 201, null, null);

        Mockito.when(officeRepository.findById(1L)).thenReturn(Mono.just(original));
        Mockito.when(officeRepository.save(any())).thenReturn(Mono.just(original));

        StepVerifier.create(officeService.update(1L, updated))
                .expectNextMatches(o -> o.getNumber().equals(201) && o.getFloor().equals(2))
                .verifyComplete();
    }

    @Test
    void shouldGetAllOffices() {
        Office office1 = new Office(1L, 101, 1, false);
        Office office2 = new Office(2L, 102, 1, false);

        Mockito.when(officeRepository.findAll()).thenReturn(Flux.just(office1, office2));

        StepVerifier.create(officeService.getAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void shouldGetOfficeById() {
        Office office = new Office(1L, 101, 1, false);
        Mockito.when(officeRepository.findById(1L)).thenReturn(Mono.just(office));

        StepVerifier.create(officeService.getById(1L))
                .expectNextMatches(o -> o.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void shouldSoftDeleteOffice() {
        Office office = new Office(1L, 101, 1, false);
        Mockito.when(officeRepository.findById(1L)).thenReturn(Mono.just(office));
        Mockito.when(officeRepository.save(any())).thenReturn(Mono.just(office));

        StepVerifier.create(officeService.delete(1L))
                .verifyComplete();
    }
}
