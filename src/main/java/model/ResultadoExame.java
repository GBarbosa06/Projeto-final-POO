package model;

import java.time.LocalDate;

public class ResultadoExame {
    private long id;
    private Exame exame;
    private String arquivo;
    private String laudo;
    private LocalDate dataResultado;

    public ResultadoExame(long id, Exame exame, String arquivo, String laudo, LocalDate dataResultado) {
        this.id = id;
        this.exame = exame;
        this.arquivo = arquivo;
        this.laudo = laudo;
        this.dataResultado = dataResultado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Exame getExame() {
        return exame;
    }

    public void setExame(Exame exame) {
        this.exame = exame;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getLaudo() {
        return laudo;
    }

    public void setLaudo(String laudo) {
        this.laudo = laudo;
    }

    public LocalDate getDataResultado() {
        return dataResultado;
    }

    public void setDataResultado(LocalDate dataResultado) {
        this.dataResultado = dataResultado;
    }
}
