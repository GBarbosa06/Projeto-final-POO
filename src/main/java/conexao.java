package br.com.hospital.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db"; 
    private static final String USUARIO = "root"; 
    private static final String SENHA = "123456"; 

    public static Connection getConexao() {
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexao com o banco estabelecida com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conexao;
    }

    public static void main(String[] args) {
        getConexao();
    }
}
