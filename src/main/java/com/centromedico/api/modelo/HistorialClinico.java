package com.centromedico.api.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "historiales_clinicos")
public class HistorialClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String diagnostico;

    private String recetaMedica;

    @OneToOne
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    private Cita cita;

    public HistorialClinico() {}

    public HistorialClinico(Long id, String diagnostico, String recetaMedica, Cita cita) {
        this.id = id;
        this.diagnostico = diagnostico;
        this.recetaMedica = recetaMedica;
        this.cita = cita;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getRecetaMedica() { return recetaMedica; }
    public void setRecetaMedica(String recetaMedica) { this.recetaMedica = recetaMedica; }

    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
}