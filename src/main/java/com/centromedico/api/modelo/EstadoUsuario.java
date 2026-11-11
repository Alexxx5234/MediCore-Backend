package com.centromedico.api.modelo;

public enum EstadoUsuario {
    PENDIENTE,   // Cuenta registrada y correo verificado, en espera de aprobación por Admin
    APROBADA,    // Cuenta aprobada por Admin (puede hacer Login y solicitar JWT)
    RECHAZADA,   // Cuenta rechazada por Admin
    BLOQUEADA    // Cuenta suspendida o bloqueada por Admin
}