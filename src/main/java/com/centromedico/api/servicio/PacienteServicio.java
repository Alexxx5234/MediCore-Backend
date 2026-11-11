package com.centromedico.api.servicio;

import com.centromedico.api.dto.PacienteDTO;
import com.centromedico.api.excepcion.ExcepcionRecursoNoEncontrado;
import com.centromedico.api.modelo.Paciente;
import com.centromedico.api.repositorio.PacienteRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServicio {

    private final PacienteRepositorio repositorio;

    public PacienteServicio(PacienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Paciente> obtenerTodos() {
        return repositorio.findAll();
    }

    public Paciente obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Paciente no encontrado con el ID: " + id));
    }

    public Paciente crear(PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setCedula(dto.getCedula());
        paciente.setTelefono(dto.getTelefono());
        return repositorio.save(paciente);
    }

    public Paciente actualizar(Long id, PacienteDTO dto) {
        Paciente paciente = obtenerPorId(id);
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setCedula(dto.getCedula());
        paciente.setTelefono(dto.getTelefono());
        return repositorio.save(paciente);
    }

    public void eliminar(Long id) {
        Paciente paciente = obtenerPorId(id);
        repositorio.delete(paciente);
    }
}