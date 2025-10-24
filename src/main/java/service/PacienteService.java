package service;

import dao.PacienteDAO;
import model.Paciente;
import java.sql.SQLException;
import java.util.List;

public class PacienteService {
    private final PacienteDAO pacienteDAO;

    public PacienteService(PacienteDAO pacienteDAO) {
        this.pacienteDAO = pacienteDAO;
    }

    public void cadastrarPaciente(Paciente paciente) throws SQLException {
        if (paciente.getNome() == null || paciente.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do paciente é obrigatório");
        }
        if (paciente.getCpf() == null || paciente.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF do paciente é obrigatório");
        }
        if (paciente.getHospital() == null) {
            throw new IllegalArgumentException("Paciente precisa estar associado a um hospital");
        }
        pacienteDAO.adicionarPaciente(paciente);
    }

    public Paciente buscarPacientePorId(long id) throws SQLException {
        return pacienteDAO.buscarPacientePorId(id);
    }

    public List<Paciente> listarTodosPacientes() throws SQLException {
        return pacienteDAO.listarTodosPacientes();
    }

    public void atualizarPaciente(Paciente paciente) throws SQLException {
        if (paciente.getId() <= 0) {
            throw new IllegalArgumentException("ID do paciente inválido");
        }
        pacienteDAO.atualizarPaciente(paciente);
    }

    public void removerPaciente(long id) throws SQLException {
        pacienteDAO.deletarPaciente(id);
    }
}
