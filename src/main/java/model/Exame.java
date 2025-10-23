package model;

import java.time.LocalDate;

public class Exame {
    private long id;
    private Paciente paciente;
    private Hospital hospital;
    private String tipoExame;
    private LocalDate dataExame;
    private StatusExame status;
}
