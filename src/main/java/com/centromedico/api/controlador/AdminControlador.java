package com.centromedico.api.controlador;

import com.centromedico.api.dto.AprobacionUsuarioDTO;
import com.centromedico.api.dto.CambiarRolDTO;
import com.centromedico.api.modelo.Usuario;
import com.centromedico.api.servicio.UsuarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "6. Administración", description = "Endpoints exclusivos para la gestión de usuarios por el Administrador")
@PreAuthorize("hasRole('ADMIN')")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    // Obtener la lista completa de usuarios
    @GetMapping("/usuarios")
    @Operation(summary = "Listar todos los usuarios del sistema")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioServicio.listarTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // Cambiar el rol de un usuario
    @PutMapping("/usuarios/{id}/rol")
    @Operation(summary = "Cambiar el rol de un usuario específico")
    public ResponseEntity<Map<String, Object>> cambiarRolUsuario(
            @PathVariable Long id,
            @Valid @RequestBody CambiarRolDTO dto) {
        
        Usuario usuarioActualizado = usuarioServicio.cambiarRolUsuario(id, dto);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Rol de usuario actualizado correctamente a " + dto.getNuevoRol());
        respuesta.put("usuario", usuarioActualizado);
        
        return ResponseEntity.ok(respuesta);
    }

    // Cambiar el estado de un usuario (PENDIENTE, APROBADA, RECHAZADA, BLOQUEADA)
    @PutMapping("/usuarios/{id}/estado")
    @Operation(summary = "Cambiar el estado de aprobación de un usuario")
    public ResponseEntity<Map<String, Object>> cambiarEstadoUsuario(
            @PathVariable Long id,
            @Valid @RequestBody AprobacionUsuarioDTO dto) {
        
        Usuario usuarioActualizado = usuarioServicio.cambiarEstadoUsuario(id, dto);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Estado de usuario actualizado correctamente a " + dto.getNuevoEstado());
        respuesta.put("usuario", usuarioActualizado);
        
        return ResponseEntity.ok(respuesta);
    }
}