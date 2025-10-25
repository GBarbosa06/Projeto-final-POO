package service;

import dao.ExameDAO;
import model.Exame;
import java.sql.SQLException;
import java.util.List;

public class ExameService {
    private final ExameDAO exameDAO;

    public ExameService(ExameDAO exameDAO) {
        this.exameDAO = exameDAO;
    }

    public void cadastrarExame(Exame exame) throws SQLException {
        if (exame.getPaciente() == null) {
            throw new IllegalArgumentException("Paciente é obrigatório");
        }
        if (exame.getHospital() == null) {
            throw new IllegalArgumentException("Hospital é obrigatório");
        }
        if (exame.getTipoExame() == null || exame.getTipoExame().isEmpty()) {
            throw new IllegalArgumentException("Tipo de exame é obrigatório");
        }
        exameDAO.adicionarExame(exame);
    }

    public Exame buscarExamePorId(long id) throws SQLException {
        return exameDAO.buscarExamePorId(id);
    }

    public List<Exame> listarTodosExames() throws SQLException {
        return exameDAO.listarTodosExames();
    }

    public void atualizarExame(Exame exame) throws SQLException {
        if (exame.getId() <= 0) {
            throw new IllegalArgumentException("ID do exame inválido");
        }
        exameDAO.atualizarExame(exame);
    }

    public void removerExame(long id) throws SQLException {
        exameDAO.deletarExame(id);
    }
}
