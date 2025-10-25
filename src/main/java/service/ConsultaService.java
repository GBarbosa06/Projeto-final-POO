package service;

import dao.ConsultaDAO;
import model.Consulta;
import java.sql.SQLException;
import java.util.List;

public class ConsultaService {
    private final ConsultaDAO consultaDAO;

    public ConsultaService(ConsultaDAO consultaDAO) {
        this.consultaDAO = consultaDAO;
    }

    public void cadastrarConsulta(Consulta consulta) throws SQLException {
        if (consulta.getPaciente() == null) {
            throw new IllegalArgumentException("Paciente é obrigatório");
        }
        if (consulta.getMedico() == null) {
            throw new IllegalArgumentException("Médico é obrigatório");
        }
        if (consulta.getHospital() == null) {
            throw new IllegalArgumentException("Hospital é obrigatório");
        }
        consultaDAO.adicionarConsulta(consulta);
    }

    public Consulta buscarConsultaPorId(long id) throws SQLException {
        return consultaDAO.buscarConsultaPorId(id);
    }

    public List<Consulta> listarTodasConsultas() throws SQLException {
        return consultaDAO.listarTodasConsultas();
    }

    public void atualizarConsulta(Consulta consulta) throws SQLException {
        if (consulta.getId() <= 0) {
            throw new IllegalArgumentException("ID da consulta inválido");
        }
        consultaDAO.atualizarConsulta(consulta);
    }

    public void removerConsulta(long id) throws SQLException {
        consultaDAO.deletarConsulta(id);
    }
}
