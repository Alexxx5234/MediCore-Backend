package com.centromedico.api.excepcion;

public class ExcepcionRecursoNoEncontrado extends RuntimeException {
    public ExcepcionRecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}