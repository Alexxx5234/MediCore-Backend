package com.centromedico.api.servicio;

import com.centromedico.api.dto.*;
import com.centromedico.api.modelo.EstadoUsuario;
import com.centromedico.api.modelo.Rol;
import com.centromedico.api.modelo.Usuario;
import com.centromedico.api.repositorio.UsuarioRepositorio;
import com.centromedico.api.seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailServicio emailServicio;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotificacionServicio notificacionServicio;

    // 1. Registro de usuario (Por defecto se asigna el rol ROLE_PACIENTE)
    @Transactional
    public Usuario registrarUsuario(RegistroRequestDTO dto) {
        if (usuarioRepositorio.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El correo electrónico ya se encuentra registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(Rol.ROLE_PACIENTE); // Asignación por defecto del dominio médico
        usuario.setEstado(EstadoUsuario.PENDIENTE);
        usuario.setEmailVerificado(false);

        // Generar codigo aleatorio de 6 dígitos
        String codigoVerificacion = String.format("%06d", new Random().nextInt(900000) + 100000);
        usuario.setCodigoVerificacion(codigoVerificacion);

        Usuario usuarioGuardado = usuarioRepositorio.save(usuario);

        notificacionServicio.avisarAdministradores("Nueva cuenta pendiente", "El usuario " + usuario.getNombre() + " (" + usuario.getEmail() + ") solicita aprobación.");

        // Enviar correo con el código
        try {
            emailServicio.enviarCodigoVerificacion(usuario.getEmail(), codigoVerificacion);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }

        return usuarioGuardado;
    }

    // Verificacion del código de 6 dígitos (Conserva el código)
    @Transactional
    public boolean verificarEmail(VerificacionEmailDTO dto) {
        Usuario usuario = usuarioRepositorio.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + dto.getEmail()));

        if (usuario.isEmailVerificado()) {
            throw new RuntimeException("El correo electrónico ya ha sido verificado anteriormente");
        }

        if (usuario.getCodigoVerificacion() != null && usuario.getCodigoVerificacion().equals(dto.getCodigo())) {
            usuario.setEmailVerificado(true);
            // Se mantiene el código en BD como registro histórico
            usuarioRepositorio.save(usuario);
            notificacionServicio.enviar(usuario, "Correo verificado", "Tu correo fue verificado. Espera la aprobación del administrador.");
            return true;
        }

        return false;
    }

    // Autenticación (Login) y emisión de Token JWT
    public JwtResponseDTO iniciarSesion(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepositorio.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        if (!usuario.isEmailVerificado()) {
            throw new RuntimeException("Debes verificar tu correo electrónico antes de ingresar");
        }

        if (usuario.getEstado() == EstadoUsuario.PENDIENTE) {
            throw new RuntimeException("Tu cuenta está verificada pero requiere la aprobación de un Administrador");
        }

        if (usuario.getEstado() == EstadoUsuario.RECHAZADA) {
            throw new RuntimeException("Tu solicitud de cuenta ha sido rechazada por el Administrador");
        }

        if (usuario.getEstado() == EstadoUsuario.BLOQUEADA) {
            throw new RuntimeException("Tu cuenta se encuentra suspendida o bloqueada");
        }

        // Generar Token JWT
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());

        return new JwtResponseDTO(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.getEstado().name()
        );
    }

    // Listar usuarios (exclusivo para Admin)
    public List<Usuario> listarTodosLosUsuarios() {
        return usuarioRepositorio.findAll();
    }

    // Cambiar el estado de un usuario (APROBADA, RECHAZADA, BLOQUEADA)
    @Transactional
    public Usuario cambiarEstadoUsuario(Long id, AprobacionUsuarioDTO dto) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (esMiCuenta(usuario)) throw new IllegalArgumentException("No puedes cambiar el estado de tu propia cuenta");
        usuario.setEstado(dto.getNuevoEstado());
        Usuario guardado = usuarioRepositorio.save(usuario);
        notificacionServicio.enviar(guardado, "Estado de tu cuenta", "Tu cuenta ahora está en estado: " + guardado.getEstado().name());
        return guardado;
    }

    // Cambiar el rol directamente recibiendo el Enum Rol
    @Transactional
    public Usuario cambiarRol(Long id, Rol nuevoRol) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (esMiCuenta(usuario)) throw new IllegalArgumentException("No puedes cambiar tu propio rol");
        usuario.setRol(nuevoRol);
        Usuario guardado = usuarioRepositorio.save(usuario);
        notificacionServicio.enviar(guardado, "Rol actualizado", "Tu nuevo rol en MediCore es: " + guardado.getRol().name());
        return guardado;
    }

    // Sobrecarga recibiendo el CambiarRolDTO desde el AdminControlador
    @Transactional
    public Usuario cambiarRolUsuario(Long id, CambiarRolDTO dto) {
        return cambiarRol(id, dto.getNuevoRol());
    }

    // Alias adicional para mantener compatibilidad con AdminControlador
    @Transactional
    public Usuario cambiarRol(Long id, CambiarRolDTO dto) {
        return cambiarRol(id, dto.getNuevoRol());
    }
    private boolean esMiCuenta(Usuario usuario) {
        org.springframework.security.core.Authentication autenticacion =
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return autenticacion != null && usuario.getEmail().equalsIgnoreCase(autenticacion.getName());
    }

}