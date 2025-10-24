package dao;

import model.Medico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {
    private Connection connection;

    public MedicoDAO(Connection connection) {
        this.connection = connection;
    }

    public void adicionarMedico(Medico medico) throws SQLException {
        String sql = "INSERT INTO medico (nome, crm, especialidade, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getCrm());
            stmt.setString(3, medico.getEspecialidade());
            stmt.setString(4, medico.getEmail());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medico.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Medico buscarMedicoPorId(long id) throws SQLException {
        String sql = "SELECT * FROM medico WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Medico(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("crm"),
                            rs.getString("especialidade"),
                            rs.getString("email")
                    );
                }
            }
        }
        return null;
    }

    public List<Medico> listarTodosMedicos() throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medico";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                medicos.add(new Medico(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("crm"),
                        rs.getString("especialidade"),
                        rs.getString("email")
                ));
            }
        }
        return medicos;
    }

    public void atualizarMedico(Medico medico) throws SQLException {
        String sql = "UPDATE medico SET nome = ?, crm = ?, especialidade = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getCrm());
            stmt.setString(3, medico.getEspecialidade());
            stmt.setString(4, medico.getEmail());
            stmt.setLong(5, medico.getId());
            stmt.executeUpdate();
        }
    }

    public void deletarMedico(long id) throws SQLException {
        String sql = "DELETE FROM medico WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
