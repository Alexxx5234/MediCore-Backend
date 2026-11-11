package com.centromedico.api.controlador;

import com.centromedico.api.dto.EspecialidadDTO;
import com.centromedico.api.modelo.Especialidad;
import com.centromedico.api.servicio.EspecialidadServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@Tag(name = "1. Especialidades", description = "CRUD de Especialidades Médicas")
public class EspecialidadControlador {

    private final EspecialidadServicio servicio;

    public EspecialidadControlador(EspecialidadServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las especialidades")
    public ResponseEntity<List<Especialidad>> listarTodas() {
        return ResponseEntity.ok(servicio.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener especialidad por ID")
    public ResponseEntity<Especialidad> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Crear nueva especialidad")
    public ResponseEntity<Especialidad> crear(@Valid @RequestBody EspecialidadDTO dto) {
        return new ResponseEntity<>(servicio.crear(dto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar especialidad")
    public ResponseEntity<Especialidad> actualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadDTO dto) {
        return ResponseEntity.ok(servicio.actualizar(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar especialidad")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}