package com.clinic.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("offices")
public class Office {
    @Id
    private Long id;

    @NotNull(message = "El número de consultorio es obligatorio")
    private Integer number;

    @NotNull(message = "El piso es obligatorio")
    private Integer floor;

    private Boolean isDeleted = false;

    public void applyUpdates(Office source) {
        if (source.getNumber() != null) this.setNumber(source.getNumber());
        if (source.getFloor() != null) this.setFloor(source.getFloor());
    }
}