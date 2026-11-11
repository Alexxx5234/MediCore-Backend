package com.centromedico.api.controlador;

import com.centromedico.api.dto.HistorialClinicoDTO;
import com.centromedico.api.modelo.HistorialClinico;
import com.centromedico.api.servicio.HistorialClinicoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historiales")
@Tag(name = "5. Historiales Clínicos", description = "CRUD de Historiales Clínicos")
public class HistorialClinicoControlador {

    private final HistorialClinicoServicio servicio;

    public HistorialClinicoControlador(HistorialClinicoServicio servicio) {
        this.servicio = servicio;
    }

    // Consultar todos los historiales: Exclusivo para ADMIN y MEDICO
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Operation(summary = "Obtener todos los historiales")
    public ResponseEntity<List<HistorialClinico>> listarTodos() {
        return ResponseEntity.ok(servicio.obtenerTodos());
    }

    // Consultar historial por ID: ADMIN, MEDICO y el PACIENTE correspondiente
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    @Operation(summary = "Obtener historial por ID")
    public ResponseEntity<HistorialClinico> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerPorId(id));
    }

    // Crear entrada en historial clínico: Exclusivo para MEDICO y ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Operation(summary = "Crear nuevo historial clínico")
    public ResponseEntity<HistorialClinico> crear(@Valid @RequestBody HistorialClinicoDTO dto) {
        return new ResponseEntity<>(servicio.crear(dto), HttpStatus.CREATED);
    }

    // Modificar historial clínico: Exclusivo para MEDICO y ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Operation(summary = "Actualizar historial clínico")
    public ResponseEntity<HistorialClinico> actualizar(@PathVariable Long id, @Valid @RequestBody HistorialClinicoDTO dto) {
        return ResponseEntity.ok(servicio.actualizar(id, dto));
    }

    // Eliminar registro clínico: Restringido únicamente a ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar historial clínico")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}