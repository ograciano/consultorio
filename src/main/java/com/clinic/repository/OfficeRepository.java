package com.clinic.repository;

import com.clinic.model.Office;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OfficeRepository extends ReactiveCrudRepository<Office,  Long> {
}