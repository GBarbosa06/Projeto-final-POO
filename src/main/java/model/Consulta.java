package model;

import java.time.LocalDate;

public class Consulta {
    private long id;
    private Paciente paciente;
    private Medico medico;
    private Hospital hospital;
    private LocalDate dataConsulta;
    private String observacoes;
    private StatusConsulta status;

    public Consulta(long id, Paciente paciente, Medico medico, Hospital hospital, LocalDate dataConsulta, String observacoes, StatusConsulta status) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.hospital = hospital;
        this.dataConsulta = dataConsulta;
        this.observacoes = observacoes;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public LocalDate getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }
}
