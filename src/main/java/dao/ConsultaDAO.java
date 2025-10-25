package dao;

import model.Consulta;
import model.Paciente;
import model.Medico;
import model.Hospital;
import model.StatusConsulta;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {
    private final Connection connection;
    private final PacienteDAO pacienteDAO;
    private final MedicoDAO medicoDAO;
    private final HospitalDAO hospitalDAO;

    public ConsultaDAO(Connection connection, PacienteDAO pacienteDAO, MedicoDAO medicoDAO, HospitalDAO hospitalDAO) {
        this.connection = connection;
        this.pacienteDAO = pacienteDAO;
        this.medicoDAO = medicoDAO;
        this.hospitalDAO = hospitalDAO;
    }

    public void adicionarConsulta(Consulta consulta) throws SQLException {
        String sql = "INSERT INTO consulta (paciente_id, medico_id, hospital_id, data_consulta, observacoes, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, consulta.getPaciente().getId());
            stmt.setLong(2, consulta.getMedico().getId());
            stmt.setLong(3, consulta.getHospital().getId());
            stmt.setDate(4, Date.valueOf(consulta.getDataConsulta()));
            stmt.setString(5, consulta.getObservacoes());
            stmt.setString(6, consulta.getStatus().name());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    consulta.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public Consulta buscarConsultaPorId(long id) throws SQLException {
        String sql = "SELECT * FROM consulta WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Paciente paciente = pacienteDAO.buscarPacientePorId(rs.getLong("paciente_id"));
                    Medico medico = medicoDAO.buscarMedicoPorId(rs.getLong("medico_id"));
                    Hospital hospital = hospitalDAO.buscarHospitalPorId(rs.getLong("hospital_id"));
                    return new Consulta(
                            rs.getLong("id"),
                            paciente,
                            medico,
                            hospital,
                            rs.getDate("data_consulta").toLocalDate(),
                            rs.getString("observacoes"),
                            StatusConsulta.valueOf(rs.getString("status"))
                    );
                }
            }
        }
        return null;
    }

    public List<Consulta> listarTodasConsultas() throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT * FROM consulta";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Paciente paciente = pacienteDAO.buscarPacientePorId(rs.getLong("paciente_id"));
                Medico medico = medicoDAO.buscarMedicoPorId(rs.getLong("medico_id"));
                Hospital hospital = hospitalDAO.buscarHospitalPorId(rs.getLong("hospital_id"));
                consultas.add(new Consulta(
                        rs.getLong("id"),
                        paciente,
                        medico,
                        hospital,
                        rs.getDate("data_consulta").toLocalDate(),
                        rs.getString("observacoes"),
                        StatusConsulta.valueOf(rs.getString("status"))
                ));
            }
        }
        return consultas;
    }

    public void atualizarConsulta(Consulta consulta) throws SQLException {
        String sql = "UPDATE consulta SET paciente_id = ?, medico_id = ?, hospital_id = ?, data_consulta = ?, observacoes = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, consulta.getPaciente().getId());
            stmt.setLong(2, consulta.getMedico().getId());
            stmt.setLong(3, consulta.getHospital().getId());
            stmt.setDate(4, Date.valueOf(consulta.getDataConsulta()));
            stmt.setString(5, consulta.getObservacoes());
            stmt.setString(6, consulta.getStatus().name());
            stmt.setLong(7, consulta.getId());
            stmt.executeUpdate();
        }
    }

    public void deletarConsulta(long id) throws SQLException {
        String sql = "DELETE FROM consulta WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
