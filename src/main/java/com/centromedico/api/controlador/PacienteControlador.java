package com.centromedico.api.controlador;

import com.centromedico.api.dto.PacienteDTO;
import com.centromedico.api.modelo.Paciente;
import com.centromedico.api.servicio.PacienteServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "3. Pacientes", description = "CRUD de Pacientes")
public class PacienteControlador {

    private final PacienteServicio servicio;

    public PacienteControlador(PacienteServicio servicio) {
        this.servicio = servicio;
    }

    // Ver listado global de pacientes: ADMIN, RECEPCION y MEDICO
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO')")
    @Operation(summary = "Obtener todos los pacientes")
    public ResponseEntity<List<Paciente>> listarTodos() {
        return ResponseEntity.ok(servicio.obtenerTodos());
    }

    // Obtener paciente por ID: ADMIN, RECEPCION, MEDICO y el propio PACIENTE
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Obtener paciente por ID")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerPorId(id));
    }

    // Crear nuevo paciente: ADMIN, RECEPCION y registro propio de PACIENTE
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'PACIENTE')")
    @Operation(summary = "Crear nuevo paciente")
    public ResponseEntity<Paciente> crear(@Valid @RequestBody PacienteDTO dto) {
        return new ResponseEntity<>(servicio.crear(dto), HttpStatus.CREATED);
    }

    // Actualizar datos del paciente: ADMIN, RECEPCION y el propio PACIENTE
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'PACIENTE')")
    @Operation(summary = "Actualizar paciente")
    public ResponseEntity<Paciente> actualizar(@PathVariable Long id, @Valid @RequestBody PacienteDTO dto) {
        return ResponseEntity.ok(servicio.actualizar(id, dto));
    }

    // Eliminar perfil de paciente: Exclusivo de ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar paciente")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}