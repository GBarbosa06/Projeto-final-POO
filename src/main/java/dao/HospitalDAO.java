package dao;

import model.Hospital;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalDAO {
    private final Connection connection;

    public HospitalDAO(Connection connection) {
        this.connection = connection;
    }

    public void adicionarHospital(Hospital hospital) throws SQLException {
        String sql = "INSERT INTO hospital (nome, cnpj, telefone) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, hospital.getNome());
            stmt.setString(2, hospital.getCnpj());
            stmt.setString(3, hospital.getTelefone());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    hospital.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Hospital buscarHospitalPorId(long id) throws SQLException {
        String sql = "SELECT * FROM hospital WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Hospital(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            null,
                            rs.getString("telefone")
                    );
                }
            }
        }
        return null;
    }

    public List<Hospital> listarTodosHospitais() throws SQLException {
        List<Hospital> hospitais = new ArrayList<>();
        String sql = "SELECT * FROM hospital";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                hospitais.add(new Hospital(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        null,
                        rs.getString("telefone")
                ));
            }
        }
        return hospitais;
    }

    public void atualizarHospital(Hospital hospital) throws SQLException {
        String sql = "UPDATE hospital SET nome = ?, cnpj = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hospital.getNome());
            stmt.setString(2, hospital.getCnpj());
            stmt.setString(3, hospital.getTelefone());
            stmt.setLong(4, hospital.getId());
            stmt.executeUpdate();
        }
    }

    public void deletarHospital(long id) throws SQLException {
        String sql = "DELETE FROM hospital WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
