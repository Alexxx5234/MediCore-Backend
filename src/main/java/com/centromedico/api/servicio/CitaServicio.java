package com.centromedico.api.servicio;

import com.centromedico.api.dto.CitaDTO;
import com.centromedico.api.excepcion.ExcepcionRecursoNoEncontrado;
import com.centromedico.api.modelo.Cita;
import com.centromedico.api.modelo.Medico;
import com.centromedico.api.modelo.Paciente;
import com.centromedico.api.repositorio.CitaRepositorio;
import com.centromedico.api.repositorio.MedicoRepositorio;
import com.centromedico.api.repositorio.PacienteRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaServicio {

    private final CitaRepositorio citaRepositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final MedicoRepositorio medicoRepositorio;

    public CitaServicio(CitaRepositorio citaRepositorio, PacienteRepositorio pacienteRepositorio, MedicoRepositorio medicoRepositorio) {
        this.citaRepositorio = citaRepositorio;
        this.pacienteRepositorio = pacienteRepositorio;
        this.medicoRepositorio = medicoRepositorio;
    }

    public List<Cita> obtenerTodas() {
        return citaRepositorio.findAll();
    }

    public Cita obtenerPorId(Long id) {
        return citaRepositorio.findById(id)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Cita no encontrada con el ID: " + id));
    }

    public Cita crear(CitaDTO dto) {
        Paciente paciente = pacienteRepositorio.findById(dto.getPacienteId())
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Paciente no encontrado con ID: " + dto.getPacienteId()));
        Medico medico = medicoRepositorio.findById(dto.getMedicoId())
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Médico no encontrado con ID: " + dto.getMedicoId()));

        Cita cita = new Cita();
        cita.setFechaHora(dto.getFechaHora());
        cita.setEstado(dto.getEstado());
        cita.setPaciente(paciente);
        cita.setMedico(medico);

        return citaRepositorio.save(cita);
    }

    public Cita actualizar(Long id, CitaDTO dto) {
        Cita cita = obtenerPorId(id);
        Paciente paciente = pacienteRepositorio.findById(dto.getPacienteId())
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Paciente no encontrado con ID: " + dto.getPacienteId()));
        Medico medico = medicoRepositorio.findById(dto.getMedicoId())
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Médico no encontrado con ID: " + dto.getMedicoId()));

        cita.setFechaHora(dto.getFechaHora());
        cita.setEstado(dto.getEstado());
        cita.setPaciente(paciente);
        cita.setMedico(medico);

        return citaRepositorio.save(cita);
    }

    public void eliminar(Long id) {
        Cita cita = obtenerPorId(id);
        citaRepositorio.delete(cita);
    }
}