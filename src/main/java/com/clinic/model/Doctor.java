package com.clinic.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("doctors")
public class Doctor {
    @Id
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido paterno es obligatorio")
    private String lastName;

    @NotBlank(message = "El apellido materno es obligatorio")
    private String middleName;

    @NotBlank(message = "La especialidad es obligatoria")
    private String specialty;

    private Boolean isDeleted = false;

    public void applyUpdates(Doctor source) {
        if (source.getFirstName() != null) this.setFirstName(source.getFirstName());
        if (source.getLastName() != null) this.setLastName(source.getLastName());
        if (source.getMiddleName() != null) this.setMiddleName(source.getMiddleName());
        if (source.getSpecialty() != null) this.setSpecialty(source.getSpecialty());
    }
}