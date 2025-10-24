package service;

import dao.MedicoDAO;
import model.Medico;
import java.sql.SQLException;
import java.util.List;

public class MedicoService {
    private final MedicoDAO medicoDAO;

    public MedicoService(MedicoDAO medicoDAO) {
        this.medicoDAO = medicoDAO;
    }

    public void cadastrarMedico(Medico medico) throws SQLException {
        if (medico.getNome() == null || medico.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do médico é obrigatório");
        }
        if (medico.getCrm() == null || medico.getCrm().isEmpty()) {
            throw new IllegalArgumentException("CRM do médico é obrigatório");
        }
        medicoDAO.adicionarMedico(medico);
    }

    public Medico buscarMedicoPorId(long id) throws SQLException {
        return medicoDAO.buscarMedicoPorId(id);
    }

    public List<Medico> listarTodosMedicos() throws SQLException {
        return medicoDAO.listarTodosMedicos();
    }

    public void atualizarMedico(Medico medico) throws SQLException {
        if (medico.getId() <= 0) {
            throw new IllegalArgumentException("ID do médico inválido");
        }
        medicoDAO.atualizarMedico(medico);
    }

    public void removerMedico(long id) throws SQLException {
        medicoDAO.deletarMedico(id);
    }
}
