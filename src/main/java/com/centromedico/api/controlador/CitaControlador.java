package com.centromedico.api.controlador;

import com.centromedico.api.dto.CitaDTO;
import com.centromedico.api.modelo.Cita;
import com.centromedico.api.servicio.CitaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "4. Citas Médicas", description = "CRUD de Citas Médicas")
public class CitaControlador {

    private final CitaServicio servicio;

    public CitaControlador(CitaServicio servicio) {
        this.servicio = servicio;
    }

    // Listar todas las citas: ADMIN y RECEPCION gestionan el calendario global; MEDICO ve su agenda
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO')")
    @Operation(summary = "Obtener todas las citas")
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(servicio.obtenerTodas());
    }

    // Ver cita especifica: ADMIN, RECEPCION, MEDICO y el PACIENTE al que pertenece la cita
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Obtener cita por ID")
    public ResponseEntity<Cita> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerPorId(id));
    }

    // Crear cita: ADMIN, RECEPCION y PACIENTE (agendamiento propio)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'PACIENTE')")
    @Operation(summary = "Crear nueva cita")
    public ResponseEntity<Cita> crear(@Valid @RequestBody CitaDTO dto) {
        return new ResponseEntity<>(servicio.crear(dto), HttpStatus.CREATED);
    }

    // Actualizar/Reagendar cita o cambiar estado: ADMIN, RECEPCION y MEDICO (ej. marcar como ATENDIDA)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO')")
    @Operation(summary = "Actualizar cita")
    public ResponseEntity<Cita> actualizar(@PathVariable Long id, @Valid @RequestBody CitaDTO dto) {
        return ResponseEntity.ok(servicio.actualizar(id, dto));
    }

    // Cancelar/Eliminar cita definitivamente: Solo ADMIN y RECEPCION
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    @Operation(summary = "Eliminar cita")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}