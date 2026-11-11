package com.centromedico.api.servicio;

import com.centromedico.api.dto.EspecialidadDTO;
import com.centromedico.api.excepcion.ExcepcionRecursoNoEncontrado;
import com.centromedico.api.modelo.Especialidad;
import com.centromedico.api.repositorio.EspecialidadRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadServicio {

    private final EspecialidadRepositorio repositorio;

    public EspecialidadServicio(EspecialidadRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Especialidad> obtenerTodas() {
        return repositorio.findAll();
    }

    public Especialidad obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Especialidad no encontrada con el ID: " + id));
    }

    public Especialidad crear(EspecialidadDTO dto) {
        Especialidad esp = new Especialidad();
        esp.setNombre(dto.getNombre());
        esp.setDescripcion(dto.getDescripcion());
        return repositorio.save(esp);
    }

    public Especialidad actualizar(Long id, EspecialidadDTO dto) {
        Especialidad esp = obtenerPorId(id);
        esp.setNombre(dto.getNombre());
        esp.setDescripcion(dto.getDescripcion());
        return repositorio.save(esp);
    }

    public void eliminar(Long id) {
        Especialidad esp = obtenerPorId(id);
        repositorio.delete(esp);
    }
}