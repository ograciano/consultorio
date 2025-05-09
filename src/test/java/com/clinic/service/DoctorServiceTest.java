package com.clinic.service;

import com.clinic.model.Doctor;
import com.clinic.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

class DoctorServiceTest {

    private DoctorRepository doctorRepository;
    private DoctorService doctorService;

    @BeforeEach
    void setup() {
        doctorRepository = Mockito.mock(DoctorRepository.class);
        doctorService = new DoctorService(doctorRepository);
    }

    @Test
    void shouldCreateDoctor() {
        Doctor doctor = new Doctor(null, "John", "Doe", "Smith", "Cardiology", false);
        Mockito.when(doctorRepository.save(any())).thenReturn(Mono.just(doctor));

        StepVerifier.create(doctorService.create(doctor))
                .expectNextMatches(d -> d.getFirstName().equals("John"))
                .verifyComplete();
    }

    @Test
    void shouldUpdateDoctor() {
        Doctor original = new Doctor(1L, "Ana", "Pérez", "López", "Pediatrics", false);
        Doctor updated = new Doctor(null, "Ana María", null, null, "Neurology", null);

        Mockito.when(doctorRepository.findById(1L)).thenReturn(Mono.just(original));
        Mockito.when(doctorRepository.save(any())).thenReturn(Mono.just(original));

        StepVerifier.create(doctorService.update(1L, updated))
                .expectNextMatches(d -> d.getFirstName().equals("Ana María") && d.getSpecialty().equals("Neurology"))
                .verifyComplete();
    }

    @Test
    void shouldGetAllDoctors() {
        Doctor doc1 = new Doctor(1L, "A", "B", "C", "X", false);
        Doctor doc2 = new Doctor(2L, "D", "E", "F", "Y", false);

        Mockito.when(doctorRepository.findAll()).thenReturn(Flux.just(doc1, doc2));

        StepVerifier.create(doctorService.getAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void shouldGetDoctorById() {
        Doctor doc = new Doctor(1L, "A", "B", "C", "X", false);
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Mono.just(doc));

        StepVerifier.create(doctorService.getById(1L))
                .expectNextMatches(d -> d.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void shouldSoftDeleteDoctor() {
        Doctor doc = new Doctor(1L, "A", "B", "C", "X", false);
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Mono.just(doc));
        Mockito.when(doctorRepository.save(any())).thenReturn(Mono.just(doc));

        StepVerifier.create(doctorService.delete(1L))
                .verifyComplete();
    }
}
