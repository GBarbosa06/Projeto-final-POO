package dao;

import model.ResultadoExame;
import model.Exame;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResultadoExameDAO {
    private final Connection connection;
    private final ExameDAO exameDAO;

    public ResultadoExameDAO(Connection connection, ExameDAO exameDAO) {
        this.connection = connection;
        this.exameDAO = exameDAO;
    }

    public void adicionarResultadoExame(ResultadoExame resultadoExame) throws SQLException {
        String sql = "INSERT INTO resultado_exame (exame_id, arquivo, laudo, data_resultado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, resultadoExame.getExame().getId());
            stmt.setString(2, resultadoExame.getArquivo());
            stmt.setString(3, resultadoExame.getLaudo());
            stmt.setDate(4, Date.valueOf(resultadoExame.getDataResultado()));
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    resultadoExame.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public ResultadoExame buscarResultadoExamePorId(long id) throws SQLException {
        String sql = "SELECT * FROM resultado_exame WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Exame exame = exameDAO.buscarExamePorId(rs.getLong("exame_id"));
                    return new ResultadoExame(
                            rs.getLong("id"),
                            exame,
                            rs.getString("arquivo"),
                            rs.getString("laudo"),
                            rs.getDate("data_resultado").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    public List<ResultadoExame> listarTodosResultadosExame() throws SQLException {
        List<ResultadoExame> resultados = new ArrayList<>();
        String sql = "SELECT * FROM resultado_exame";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Exame exame = exameDAO.buscarExamePorId(rs.getLong("exame_id"));
                resultados.add(new ResultadoExame(
                        rs.getLong("id"),
                        exame,
                        rs.getString("arquivo"),
                        rs.getString("laudo"),
                        rs.getDate("data_resultado").toLocalDate()
                ));
            }
        }
        return resultados;
    }

    public void atualizarResultadoExame(ResultadoExame resultadoExame) throws SQLException {
        String sql = "UPDATE resultado_exame SET exame_id = ?, arquivo = ?, laudo = ?, data_resultado = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, resultadoExame.getExame().getId());
            stmt.setString(2, resultadoExame.getArquivo());
            stmt.setString(3, resultadoExame.getLaudo());
            stmt.setDate(4, Date.valueOf(resultadoExame.getDataResultado()));
            stmt.setLong(5, resultadoExame.getId());
            stmt.executeUpdate();
        }
    }

    public void deletarResultadoExame(long id) throws SQLException {
        String sql = "DELETE FROM resultado_exame WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
