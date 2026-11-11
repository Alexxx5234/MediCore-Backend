package com.centromedico.api.servicio;

import com.centromedico.api.dto.HistorialClinicoDTO;
import com.centromedico.api.excepcion.ExcepcionRecursoNoEncontrado;
import com.centromedico.api.modelo.Cita;
import com.centromedico.api.modelo.HistorialClinico;
import com.centromedico.api.repositorio.CitaRepositorio;
import com.centromedico.api.repositorio.HistorialClinicoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialClinicoServicio {

    private final HistorialClinicoRepositorio historialRepositorio;
    private final CitaRepositorio citaRepositorio;

    public HistorialClinicoServicio(HistorialClinicoRepositorio historialRepositorio, CitaRepositorio citaRepositorio) {
        this.historialRepositorio = historialRepositorio;
        this.citaRepositorio = citaRepositorio;
    }

    public List<HistorialClinico> obtenerTodos() {
        return historialRepositorio.findAll();
    }

    public HistorialClinico obtenerPorId(Long id) {
        return historialRepositorio.findById(id)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Historial clínico no encontrado con el ID: " + id));
    }

    public HistorialClinico crear(HistorialClinicoDTO dto) {
        Cita cita = citaRepositorio.findById(dto.getCitaId())
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Cita no encontrada con ID: " + dto.getCitaId()));

        HistorialClinico historial = new HistorialClinico();
        historial.setDiagnostico(dto.getDiagnostico());
        historial.setRecetaMedica(dto.getRecetaMedica());
        historial.setCita(cita);

        return historialRepositorio.save(historial);
    }

    public HistorialClinico actualizar(Long id, HistorialClinicoDTO dto) {
        HistorialClinico historial = obtenerPorId(id);
        Cita cita = citaRepositorio.findById(dto.getCitaId())
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Cita no encontrada con ID: " + dto.getCitaId()));

        historial.setDiagnostico(dto.getDiagnostico());
        historial.setRecetaMedica(dto.getRecetaMedica());
        historial.setCita(cita);

        return historialRepositorio.save(historial);
    }

    public void eliminar(Long id) {
        HistorialClinico historial = obtenerPorId(id);
        historialRepositorio.delete(historial);
    }
}