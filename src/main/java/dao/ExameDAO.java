package dao;

import model.Exame;
import model.Paciente;
import model.Hospital;
import model.StatusExame;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExameDAO {
    private final Connection connection;
    private final PacienteDAO pacienteDAO;
    private final HospitalDAO hospitalDAO;

    public ExameDAO(Connection connection, PacienteDAO pacienteDAO, HospitalDAO hospitalDAO) {
        this.connection = connection;
        this.pacienteDAO = pacienteDAO;
        this.hospitalDAO = hospitalDAO;
    }

    public void adicionarExame(Exame exame) throws SQLException {
        String sql = "INSERT INTO exame (paciente_id, hospital_id, tipo_exame, data_exame, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, exame.getPaciente().getId());
            stmt.setLong(2, exame.getHospital().getId());
            stmt.setString(3, exame.getTipoExame());
            stmt.setDate(4, Date.valueOf(exame.getDataExame()));
            stmt.setString(5, exame.getStatus().name());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exame.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Exame buscarExamePorId(long id) throws SQLException {
        String sql = "SELECT * FROM exame WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Paciente paciente = pacienteDAO.buscarPacientePorId(rs.getLong("paciente_id"));
                    Hospital hospital = hospitalDAO.buscarHospitalPorId(rs.getLong("hospital_id"));
                    return new Exame(
                            rs.getLong("id"),
                            paciente,
                            hospital,
                            rs.getString("tipo_exame"),
                            rs.getDate("data_exame").toLocalDate(),
                            StatusExame.valueOf(rs.getString("status"))
                    );
                }
            }
        }
        return null;
    }

    public List<Exame> listarTodosExames() throws SQLException {
        List<Exame> exames = new ArrayList<>();
        String sql = "SELECT * FROM exame";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Paciente paciente = pacienteDAO.buscarPacientePorId(rs.getLong("paciente_id"));
                Hospital hospital = hospitalDAO.buscarHospitalPorId(rs.getLong("hospital_id"));
                exames.add(new Exame(
                        rs.getLong("id"),
                        paciente,
                        hospital,
                        rs.getString("tipo_exame"),
                        rs.getDate("data_exame").toLocalDate(),
                        StatusExame.valueOf(rs.getString("status"))
                ));
            }
        }
        return exames;
    }

    public void atualizarExame(Exame exame) throws SQLException {
        String sql = "UPDATE exame SET paciente_id = ?, hospital_id = ?, tipo_exame = ?, data_exame = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, exame.getPaciente().getId());
            stmt.setLong(2, exame.getHospital().getId());
            stmt.setString(3, exame.getTipoExame());
            stmt.setDate(4, Date.valueOf(exame.getDataExame()));
            stmt.setString(5, exame.getStatus().name());
            stmt.setLong(6, exame.getId());
            stmt.executeUpdate();
        }
    }

    public void deletarExame(long id) throws SQLException {
        String sql = "DELETE FROM exame WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
