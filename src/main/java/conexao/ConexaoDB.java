package conexao;

import java.sql.Connection;

public interface ConexaoDB {
    Connection obterConexao() throws Exception;

    void fecharConexao(Connection conexao);
}
