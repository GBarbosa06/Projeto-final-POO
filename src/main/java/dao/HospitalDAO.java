package dao;

import model.Hospital;
import model.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalDAO {
    private final Connection connection;

    public HospitalDAO(Connection connection) {
        this.connection = connection;
    }

    public void adicionarHospital(Hospital hospital) throws SQLException {
        String sql = "INSERT INTO hospital (nome, cnpj, telefone, rua, numero, bairro, cidade, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, hospital.getNome());
            stmt.setString(2, hospital.getCnpj());
            stmt.setString(3, hospital.getTelefone());
            stmt.setString(4, hospital.getEndereco().getRua());
            stmt.setString(5, hospital.getEndereco().getNumero());
            stmt.setString(6, hospital.getEndereco().getBairro());
            stmt.setString(7, hospital.getEndereco().getCidade());
            stmt.setString(8, hospital.getEndereco().getCep());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    hospital.setId(keys.getLong(1));
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
                    Endereco endereco = new Endereco(
                            rs.getString("rua"),
                            rs.getString("numero"),
                            rs.getString("bairro"),
                            rs.getString("cidade"),
                            rs.getString("cep")
                    );
                    return new Hospital(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            endereco,
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
                Endereco endereco = new Endereco(
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("cep")
                );
                hospitais.add(new Hospital(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        endereco,
                        rs.getString("telefone")
                ));
            }
        }
        return hospitais;
    }

    public void atualizarHospital(Hospital hospital) throws SQLException {
        String sql = "UPDATE hospital SET nome = ?, cnpj = ?, telefone = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, cep = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hospital.getNome());
            stmt.setString(2, hospital.getCnpj());
            stmt.setString(3, hospital.getTelefone());
            stmt.setString(4, hospital.getEndereco().getRua());
            stmt.setString(5, hospital.getEndereco().getNumero());
            stmt.setString(6, hospital.getEndereco().getBairro());
            stmt.setString(7, hospital.getEndereco().getCidade());
            stmt.setString(8, hospital.getEndereco().getCep());
            stmt.setLong(9, hospital.getId());
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
