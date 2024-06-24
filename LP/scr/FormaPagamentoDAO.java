
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class FormaPagamentoDAO {
    public List<String> getFormasPagamento() {
        List<String> formasPagamento = new ArrayList<>();
        String query = "SELECT nome FROM formas_pagamento";

        try (Connection connection = ConexaoMySQL.conectar();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                formasPagamento.add(resultSet.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return formasPagamento;
    }
}
