package com.centromedico.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class HistorialClinicoDTO {

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(min = 3, message = "El diagnóstico debe tener al menos 3 caracteres")
    private String diagnostico;

    private String recetaMedica;

    @NotNull(message = "El ID de la cita es obligatorio")
    private Long citaId;

    public HistorialClinicoDTO() {}

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getRecetaMedica() { return recetaMedica; }
    public void setRecetaMedica(String recetaMedica) { this.recetaMedica = recetaMedica; }

    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
}