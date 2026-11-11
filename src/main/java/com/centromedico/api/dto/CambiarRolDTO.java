package com.centromedico.api.dto;

import com.centromedico.api.modelo.Rol;
import jakarta.validation.constraints.NotNull;

public class CambiarRolDTO {

    @NotNull(message = "El nuevo rol es obligatorio")
    private Rol nuevoRol;

    public CambiarRolDTO() {
    }

    public CambiarRolDTO(Rol nuevoRol) {
        this.nuevoRol = nuevoRol;
    }

    public Rol getNuevoRol() {
        return nuevoRol;
    }

    public void setNuevoRol(Rol nuevoRol) {
        this.nuevoRol = nuevoRol;
    }
}