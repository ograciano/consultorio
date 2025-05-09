package com.clinic.controller;

import com.clinic.model.Office;
import com.clinic.service.OfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/offices")
@RequiredArgsConstructor
@Validated
public class OfficeController {
    private final OfficeService officeService;

    @GetMapping
    public Flux<Office> getAll() {
        return officeService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Office> getById(@PathVariable Long id) {
        return officeService.getById(id);
    }

    @PostMapping
    public Mono<Office> create(@RequestBody @Valid Office office) {
        return officeService.create(office);
    }

    @PutMapping("/{id}")
    public Mono<Office> update(@PathVariable Long id, @RequestBody Office office) {
        return officeService.update(id, office);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return officeService.delete(id);
    }
}