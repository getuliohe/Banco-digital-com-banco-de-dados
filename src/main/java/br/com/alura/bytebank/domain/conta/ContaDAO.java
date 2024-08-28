package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContaDAO {

    private Connection conn;

    ContaDAO(Connection connection){
        this.conn = connection;
    }

    public void salvar(DadosAberturaConta dadosDaConta) throws SQLException {
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);

        String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email)"
                + "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = this.conn.prepareStatement(sql);

        preparedStatement.setInt(1,conta.getNumero());
        preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
        preparedStatement.setString(3,dadosDaConta.dadosCliente().nome());
        preparedStatement.setString(4,dadosDaConta.dadosCliente().cpf());
        preparedStatement.setString(5,dadosDaConta.dadosCliente().email());

        preparedStatement.execute();
        preparedStatement.close();
    }
}
