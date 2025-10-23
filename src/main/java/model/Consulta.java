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
}
