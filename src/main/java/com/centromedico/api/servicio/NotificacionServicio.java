package com.centromedico.api.servicio;

import com.centromedico.api.modelo.Notificacion;
import com.centromedico.api.modelo.Usuario;
import com.centromedico.api.modelo.Rol;
import com.centromedico.api.repositorio.NotificacionRepositorio;
import com.centromedico.api.repositorio.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class NotificacionServicio {
    private final NotificacionRepositorio repositorio;
    private final UsuarioRepositorio usuarios;

    public NotificacionServicio(NotificacionRepositorio repositorio, UsuarioRepositorio usuarios) {
        this.repositorio = repositorio;
        this.usuarios = usuarios;
    }

    @Transactional
    public void enviar(Usuario destino, String titulo, String mensaje) {
        Notificacion aviso = new Notificacion();
        aviso.setUsuario(destino);
        aviso.setTitulo(titulo);
        aviso.setMensaje(mensaje);
        repositorio.save(aviso);
    }

    @Transactional
    public void avisarAdministradores(String titulo, String mensaje) {
        for (Usuario u : usuarios.findAll()) {
            if (u.getRol() == Rol.ROLE_ADMIN) enviar(u, titulo, mensaje);
        }
    }

    @Transactional(readOnly = true)
    public List<Notificacion> listar(Long idUsuario) {
        return repositorio.findByUsuarioIdOrderByFechaCreacionDesc(idUsuario);
    }

    @Transactional
    public Notificacion marcarLeida(Long idNotificacion, Long idUsuario) {
        Notificacion n = repositorio.findByIdAndUsuarioId(idNotificacion, idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada"));
        n.setLeida(true);
        return repositorio.save(n);
    }
}
