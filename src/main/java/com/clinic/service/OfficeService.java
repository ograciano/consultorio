package com.clinic.service;

import com.clinic.model.Office;
import com.clinic.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OfficeService {
    // This class will contain methods to manage office data
    // For example, methods to create, update, delete, and retrieve office information

    private final OfficeRepository officeRepository;

    public Flux<Office> getAll() {
        return officeRepository.findAll()
                .filter(office -> !Boolean.TRUE.equals(office.getIsDeleted()));
    }

    public Mono<Office> getById(Long id) {
        return officeRepository.findById(id)
                .filter(office -> !Boolean.TRUE.equals(office.getIsDeleted()));
    }

    public Mono<Office> create(Office office) {
        return officeRepository.save(office);
    }

    public Mono<Office> update(Long id, Office office) {
        return officeRepository.findById(id)
                .flatMap(existingOffice -> {
                    existingOffice.applyUpdates(office);
                    return officeRepository.save(existingOffice);
                });
    }

    public Mono<Void> delete(Long id) {
        return officeRepository.findById(id)
                .flatMap(existingOffice -> {
                    existingOffice.setIsDeleted(true);
                    return officeRepository.save(existingOffice).then();
                });
    }
}