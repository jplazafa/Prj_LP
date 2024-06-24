import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProdutoDAO {
    public void adicionarProduto(Produto produto) {
        String sql = "INSERT INTO produtos (nome, descricao, preco, estoque) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoMySQL.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar produto", e);
        }
    }
}
