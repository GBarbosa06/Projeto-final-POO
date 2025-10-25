package dao;

import model.Paciente;
import model.Hospital;
import model.Endereco;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {
    private final Connection connection;
    private final HospitalDAO hospitalDAO;

    public PacienteDAO(Connection connection, HospitalDAO hospitalDAO) {
        this.connection = connection;
        this.hospitalDAO = hospitalDAO;
    }

    public void adicionarPaciente(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (hospital_id, nome, cpf, data_nascimento, telefone, email, rua, numero, bairro, cidade, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, paciente.getHospital().getId());
            stmt.setString(2, paciente.getNome());
            stmt.setString(3, paciente.getCpf());
            stmt.setDate(4, Date.valueOf(paciente.getDataNascimento()));
            stmt.setString(5, paciente.getTelefone());
            stmt.setString(6, paciente.getEmail());
            stmt.setString(7, paciente.getEndereco().getRua());
            stmt.setString(8, paciente.getEndereco().getNumero());
            stmt.setString(9, paciente.getEndereco().getBairro());
            stmt.setString(10, paciente.getEndereco().getCidade());
            stmt.setString(11, paciente.getEndereco().getCep());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    paciente.setId(keys.getLong(1));
                }
            }
        }
    }

    public Paciente buscarPacientePorId(long id) throws SQLException {
        String sql = "SELECT * FROM paciente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Hospital hospital = hospitalDAO.buscarHospitalPorId(rs.getLong("hospital_id"));
                    Endereco endereco = new Endereco(
                            rs.getString("rua"),
                            rs.getString("numero"),
                            rs.getString("bairro"),
                            rs.getString("cidade"),
                            rs.getString("cep")
                    );
                    return new Paciente(
                            rs.getLong("id"),
                            hospital,
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getDate("data_nascimento").toLocalDate(),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            endereco
                    );
                }
            }
        }
        return null;
    }

    public List<Paciente> listarTodosPacientes() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Hospital hospital = hospitalDAO.buscarHospitalPorId(rs.getLong("hospital_id"));
                Endereco endereco = new Endereco(
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("cep")
                );
                pacientes.add(new Paciente(
                        rs.getLong("id"),
                        hospital,
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        endereco
                ));
            }
        }
        return pacientes;
    }

    public void atualizarPaciente(Paciente paciente) throws SQLException {
        String sql = "UPDATE paciente SET hospital_id = ?, nome = ?, cpf = ?, data_nascimento = ?, telefone = ?, email = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, cep = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, paciente.getHospital().getId());
            stmt.setString(2, paciente.getNome());
            stmt.setString(3, paciente.getCpf());
            stmt.setDate(4, Date.valueOf(paciente.getDataNascimento()));
            stmt.setString(5, paciente.getTelefone());
            stmt.setString(6, paciente.getEmail());
            stmt.setString(7, paciente.getEndereco().getRua());
            stmt.setString(8, paciente.getEndereco().getNumero());
            stmt.setString(9, paciente.getEndereco().getBairro());
            stmt.setString(10, paciente.getEndereco().getCidade());
            stmt.setString(11, paciente.getEndereco().getCep());
            stmt.setLong(12, paciente.getId());
            stmt.executeUpdate();
        }
    }

    public void deletarPaciente(long id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
