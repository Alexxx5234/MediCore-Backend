package com.centromedico.api.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GestorExcepcionesGlobal {

    @ExceptionHandler(ExcepcionRecursoNoEncontrado.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontrado(ExcepcionRecursoNoEncontrado ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", ex.getMessage());
        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
    }

    // Captura cuando el campo viene NULO o VACÍO (@NotNull, @NotBlank, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> respuesta = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            respuesta.put("error", error.getDefaultMessage())
        );
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // Captura cuando mandan un texto que NO EXISTE en el Enum (ej: "INVENTADO")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarEnumInexistente(HttpMessageNotReadableException ex) {
        Map<String, String> respuesta = new HashMap<>();
        String mensaje = ex.getMessage();

        if (mensaje != null && mensaje.contains("EstadoUsuario")) {
            respuesta.put("error", "Estado no existente. Los estados válidos son: PENDIENTE, APROBADA, RECHAZADA, BLOQUEADA.");
        } else if (mensaje != null && mensaje.contains("Rol")) {
            respuesta.put("error", "Rol no existente. Los roles válidos son: ROLE_ADMIN, ROLE_MEDICO, ROLE_RECEPCION, ROLE_PACIENTE.");
        } else {
            respuesta.put("error", "El campo enviado o formato JSON no es válido.");
        }

        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // Captura cuando un usuario no tiene los permisos requeridos (403 Forbidden)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> manejarAccesoDenegado(AccessDeniedException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Acceso denegado: Solo un usuario con rol ADMIN puede cambiar el rol o estado de un usuario.");
        return new ResponseEntity<>(respuesta, HttpStatus.FORBIDDEN);
    }
}