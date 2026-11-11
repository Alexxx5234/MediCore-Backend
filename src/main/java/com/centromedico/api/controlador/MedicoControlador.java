package com.centromedico.api.controlador;

import com.centromedico.api.dto.MedicoDTO;
import com.centromedico.api.modelo.Medico;
import com.centromedico.api.servicio.MedicoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@Tag(name = "2. Médicos", description = "CRUD de Médicos")
public class MedicoControlador {

    private final MedicoServicio servicio;

    public MedicoControlador(MedicoServicio servicio) {
        this.servicio = servicio;
    }

    // Ver catálogo/directorio de médicos: Todos los roles autenticados
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Obtener todos los médicos")
    public ResponseEntity<List<Medico>> listarTodos() {
        return ResponseEntity.ok(servicio.obtenerTodos());
    }

    // Ver información detallada del médico por ID: Todos los roles
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Obtener médico por ID")
    public ResponseEntity<Medico> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerPorId(id));
    }

    // Registrar perfil del médico: ADMIN y RECEPCION
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    @Operation(summary = "Crear nuevo médico")
    public ResponseEntity<Medico> crear(@Valid @RequestBody MedicoDTO dto) {
        return new ResponseEntity<>(servicio.crear(dto), HttpStatus.CREATED);
    }

    // Actualizar datos del médico: ADMIN y el propio MEDICO
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Operation(summary = "Actualizar médico")
    public ResponseEntity<Medico> actualizar(@PathVariable Long id, @Valid @RequestBody MedicoDTO dto) {
        return ResponseEntity.ok(servicio.actualizar(id, dto));
    }

    // Dar de baja/eliminar médico: Exclusivo de ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar médico")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}