package com.centromedico.api.controlador;

import com.centromedico.api.modelo.Notificacion;
import com.centromedico.api.modelo.Usuario;
import com.centromedico.api.repositorio.UsuarioRepositorio;
import com.centromedico.api.servicio.NotificacionServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionControlador {
    private final NotificacionServicio servicio;
    private final UsuarioRepositorio usuarios;
    public NotificacionControlador(NotificacionServicio servicio, UsuarioRepositorio usuarios) {
        this.servicio = servicio;
        this.usuarios = usuarios;
    }
    public record AvisoDTO(Long id, String titulo, String mensaje, boolean leida, LocalDateTime fechaCreacion) {
        public static AvisoDTO desde(Notificacion n) {
            return new AvisoDTO(n.getId(), n.getTitulo(), n.getMensaje(), n.isLeida(), n.getFechaCreacion());
        }
    }
    private Usuario actual(Authentication autenticacion) {
        if (autenticacion == null || !autenticacion.isAuthenticated())
            throw new org.springframework.security.access.AccessDeniedException("No autenticado");
        return usuarios.findByEmail(autenticacion.getName())
            .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Usuario no encontrado"));
    }
    @GetMapping
    public List<AvisoDTO> listar(Authentication autenticacion) {
        return servicio.listar(actual(autenticacion).getId()).stream().map(AvisoDTO::desde).toList();
    }
    @PutMapping("/{id}/leer")
    public ResponseEntity<AvisoDTO> marcarLeida(@PathVariable Long id, Authentication autenticacion) {
        return ResponseEntity.ok(AvisoDTO.desde(servicio.marcarLeida(id, actual(autenticacion).getId())));
    }
}
