package com.centromedico.api.repositorio;

import com.centromedico.api.modelo.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialClinicoRepositorio extends JpaRepository<HistorialClinico, Long> {
}