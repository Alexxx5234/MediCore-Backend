package com.centromedico.api.repositorio;

import com.centromedico.api.modelo.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NotificacionRepositorio extends JpaRepository<Notificacion,Long> {
    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
    Optional<Notificacion> findByIdAndUsuarioId(Long id, Long usuarioId);
}
