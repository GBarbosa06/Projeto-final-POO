package model;

import java.time.LocalDate;

public class Exame {
    private long id;
    private Paciente paciente;
    private Hospital hospital;
    private String tipoExame;
    private LocalDate dataExame;
    private StatusExame status;

    public Exame(long id, Paciente paciente, Hospital hospital, String tipoExame, LocalDate dataExame, StatusExame status) {
        this.id = id;
        this.paciente = paciente;
        this.hospital = hospital;
        this.tipoExame = tipoExame;
        this.dataExame = dataExame;
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

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(String tipoExame) {
        this.tipoExame = tipoExame;
    }

    public LocalDate getDataExame() {
        return dataExame;
    }

    public void setDataExame(LocalDate dataExame) {
        this.dataExame = dataExame;
    }

    public StatusExame getStatus() {
        return status;
    }

    public void setStatus(StatusExame status) {
        this.status = status;
    }
}
