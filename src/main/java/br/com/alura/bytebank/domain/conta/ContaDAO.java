package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {

    private Connection conn;

    ContaDAO(Connection connection){
        this.conn = connection;
    }

    public void salvar(DadosAberturaConta dadosDaConta) throws SQLException {
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), BigDecimal.ZERO, cliente);

        String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email)"
                + "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = this.conn.prepareStatement(sql);

        preparedStatement.setInt(1,conta.getNumero());
        preparedStatement.setBigDecimal(2, conta.getSaldo());
        preparedStatement.setString(3,dadosDaConta.dadosCliente().nome());
        preparedStatement.setString(4,dadosDaConta.dadosCliente().cpf());
        preparedStatement.setString(5,dadosDaConta.dadosCliente().email());

        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
    }

    public Set<Conta> listar() throws SQLException {
        Set<Conta> contas = new HashSet<>();

        String sql = "SELECT * FROM conta";

        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            int numero = resultSet.getInt(1);
            BigDecimal saldo = resultSet.getBigDecimal(2);
            String nome = resultSet.getString(3);
            String cpf = resultSet.getString(4);
            String email = resultSet.getString(5);

            DadosCadastroCliente dadosCadastroCliente =
                    new DadosCadastroCliente(nome, cpf, email);

            Cliente cliente = new Cliente(dadosCadastroCliente);
            contas.add(new Conta(numero, saldo, cliente));
        }
        resultSet.close();
        ps.close();
        conn.close();

        return contas;
    }

    public Conta listarPorNumero(Integer numero) {
        String sql = "SELECT * FROM conta WHERE numero = ?";

        PreparedStatement ps;
        ResultSet resultSet;
        Conta conta = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, numero);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Integer numeroRecuperado = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                conta = new Conta(numeroRecuperado, saldo, cliente);
            }
            resultSet.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conta;
    }

    public void alterar(int numero, BigDecimal valor) throws SQLException {
        String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";
        PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
        preparedStatement.setBigDecimal(1, valor);
        preparedStatement.setInt(2, numero);

        preparedStatement.execute();
        preparedStatement.close();
        conn.close();
    }

    public void deletar(Integer numeroDaConta) throws SQLException {
        String sql = "DELETE FROM conta WHERE numero = ?";

        PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
        preparedStatement.setInt(1, numeroDaConta);

        preparedStatement.execute();
        preparedStatement.close();
        conn.close();

    }
}
