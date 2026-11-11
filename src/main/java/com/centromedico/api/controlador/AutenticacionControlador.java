package com.centromedico.api.controlador;

import com.centromedico.api.dto.*;
import com.centromedico.api.modelo.Usuario;
import com.centromedico.api.servicio.UsuarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "7. Autenticación", description = "Endpoints públicos para registro, verificación de correo y login")
public class AutenticacionControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/registro")
    @Operation(summary = "Registrar un nuevo usuario en la plataforma")
    public ResponseEntity<Map<String, Object>> registrarUsuario(@Valid @RequestBody RegistroRequestDTO dto) {
        Usuario usuario = usuarioServicio.registrarUsuario(dto);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Usuario registrado exitosamente. Se ha enviado un código de verificación a su correo electrónico.");
        respuesta.put("email", usuario.getEmail());
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @PostMapping("/verificar")
    @Operation(summary = "Verificar código de confirmación enviado por correo")
    public ResponseEntity<Map<String, String>> verificarEmail(@Valid @RequestBody VerificacionEmailDTO dto) {
        boolean verificado = usuarioServicio.verificarEmail(dto);
        Map<String, String> respuesta = new HashMap<>();
        
        if (verificado) {
            respuesta.put("mensaje", "Correo electrónico verificado exitosamente. Tu cuenta está en espera de aprobación por un Administrador.");
            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.put("error", "El código de verificación ingresado es incorrecto.");
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener Token JWT")
    public ResponseEntity<JwtResponseDTO> iniciarSesion(@Valid @RequestBody LoginRequestDTO dto) {
        JwtResponseDTO jwtResponse = usuarioServicio.iniciarSesion(dto);
        return ResponseEntity.ok(jwtResponse);
    }
}