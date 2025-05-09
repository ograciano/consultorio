package com.clinic.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("appointments")
public class Appointment {
    @Id
    private Long id;

    @NotNull(message = "El ID del doctor es obligatorio")
    private Long doctorId;

    @NotNull(message = "El ID del consultorio es obligatorio")
    private Long officeId;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String patientName;

    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    private LocalDateTime appointmentTime;

    @NotNull(message = "El estado es obligatorio")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    private Boolean isDeleted = false;

    public void applyUpdates(Appointment source) {
        if (source.getDoctorId() != null) this.setDoctorId(source.getDoctorId());
        if (source.getOfficeId() != null) this.setOfficeId(source.getOfficeId());
        if (source.getPatientName() != null) this.setPatientName(source.getPatientName());
        if (source.getAppointmentTime() != null) this.setAppointmentTime(source.getAppointmentTime());
        if (source.getStatus() != null) this.setStatus(source.getStatus());
    }

    public enum AppointmentStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }
}

