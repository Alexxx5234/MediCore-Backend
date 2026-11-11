package com.centromedico.api.servicio;

import com.centromedico.api.dto.MedicoDTO;
import com.centromedico.api.excepcion.ExcepcionRecursoNoEncontrado;
import com.centromedico.api.modelo.Especialidad;
import com.centromedico.api.modelo.Medico;
import com.centromedico.api.repositorio.EspecialidadRepositorio;
import com.centromedico.api.repositorio.MedicoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoServicio {

    private final MedicoRepositorio medicoRepositorio;
    private final EspecialidadRepositorio especialidadRepositorio;

    public MedicoServicio(MedicoRepositorio medicoRepositorio, EspecialidadRepositorio especialidadRepositorio) {
        this.medicoRepositorio = medicoRepositorio;
        this.especialidadRepositorio = especialidadRepositorio;
    }

    public List<Medico> obtenerTodos() {
        return medicoRepositorio.findAll();
    }

    public Medico obtenerPorId(Long id) {
        return medicoRepositorio.findById(id)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Médico no encontrado con el ID: " + id));
    }

    public Medico crear(MedicoDTO dto) {
        Especialidad especialidad = especialidadRepositorio.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Especialidad no encontrada con ID: " + dto.getEspecialidadId()));

        Medico medico = new Medico();
        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setEspecialidad(especialidad);

        return medicoRepositorio.save(medico);
    }

    public Medico actualizar(Long id, MedicoDTO dto) {
        Medico medico = obtenerPorId(id);
        Especialidad especialidad = especialidadRepositorio.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Especialidad no encontrada con ID: " + dto.getEspecialidadId()));

        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setEspecialidad(especialidad);

        return medicoRepositorio.save(medico);
    }

    public void eliminar(Long id) {
        Medico medico = obtenerPorId(id);
        medicoRepositorio.delete(medico);
    }
}