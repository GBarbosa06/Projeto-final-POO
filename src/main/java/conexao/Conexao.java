package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao implements ConexaoDB{
    private final String URL = "jdbc:mysql://localhost:3306/hospital_db";
    private final String USUARIO = "root";
    private final String SENHA = "123456";

    @Override
    public Connection obterConexao() {
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexao com o banco estabelecida com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conexao;
    }

    @Override
    public void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}
