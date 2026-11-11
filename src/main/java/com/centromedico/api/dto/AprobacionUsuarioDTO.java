package com.centromedico.api.dto;

import com.centromedico.api.modelo.EstadoUsuario;
import jakarta.validation.constraints.NotNull;

public class AprobacionUsuarioDTO {

    @NotNull(message = "El nuevo estado del usuario es obligatorio")
    private EstadoUsuario nuevoEstado;

    public AprobacionUsuarioDTO() {
    }

    public AprobacionUsuarioDTO(EstadoUsuario nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public EstadoUsuario getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(EstadoUsuario nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }
}