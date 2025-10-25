package service;

import dao.ResultadoExameDAO;
import model.ResultadoExame;
import java.sql.SQLException;
import java.util.List;

public class ResultadoExameService {
    private final ResultadoExameDAO resultadoExameDAO;

    public ResultadoExameService(ResultadoExameDAO resultadoExameDAO) {
        this.resultadoExameDAO = resultadoExameDAO;
    }

    public void cadastrarResultadoExame(ResultadoExame resultadoExame) throws SQLException {
        if (resultadoExame.getExame() == null) {
            throw new IllegalArgumentException("Exame é obrigatório");
        }
        if (resultadoExame.getLaudo() == null || resultadoExame.getLaudo().isEmpty()) {
            throw new IllegalArgumentException("Laudo é obrigatório");
        }
        resultadoExameDAO.adicionarResultadoExame(resultadoExame);
    }

    public ResultadoExame buscarResultadoExamePorId(long id) throws SQLException {
        return resultadoExameDAO.buscarResultadoExamePorId(id);
    }

    public List<ResultadoExame> listarTodosResultadosExame() throws SQLException {
        return resultadoExameDAO.listarTodosResultadosExame();
    }

    public void atualizarResultadoExame(ResultadoExame resultadoExame) throws SQLException {
        if (resultadoExame.getId() <= 0) {
            throw new IllegalArgumentException("ID do resultado de exame inválido");
        }
        resultadoExameDAO.atualizarResultadoExame(resultadoExame);
    }

    public void removerResultadoExame(long id) throws SQLException {
        resultadoExameDAO.deletarResultadoExame(id);
    }
}
