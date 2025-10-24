package service;

import dao.HospitalDAO;
import model.Hospital;
import java.sql.SQLException;
import java.util.List;

public class HospitalService {
    private final HospitalDAO hospitalDAO;

    public HospitalService(HospitalDAO hospitalDAO) {
        this.hospitalDAO = hospitalDAO;
    }

    public void cadastrarHospital(Hospital hospital) throws SQLException {
        if (hospital.getNome() == null || hospital.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do hospital é obrigatório");
        }
        if (hospital.getCnpj() == null || hospital.getCnpj().isEmpty()) {
            throw new IllegalArgumentException("CNPJ do hospital é obrigatório");
        }
        hospitalDAO.adicionarHospital(hospital);
    }

    public Hospital buscarHospitalPorId(long id) throws SQLException {
        return hospitalDAO.buscarHospitalPorId(id);
    }

    public List<Hospital> listarTodosHospitais() throws SQLException {
        return hospitalDAO.listarTodosHospitais();
    }

    public void atualizarHospital(Hospital hospital) throws SQLException {
        if (hospital.getId() <= 0) {
            throw new IllegalArgumentException("ID do hospital inválido");
        }
        hospitalDAO.atualizarHospital(hospital);
    }

    public void removerHospital(long id) throws SQLException {
        hospitalDAO.deletarHospital(id);
    }
}
