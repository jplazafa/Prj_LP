import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TelaPagamento extends JFrame {

    private JPanel painel;
    private JTable tabela;
    private JScrollPane scrollPane;
    private JLabel labelTitulo;
    private JLabel labelFormaPagamento;
    private JComboBox<String> comboPagamento;
    private JButton botaoFinalizar;
    private List<String[]> itensCarrinho;

    public TelaPagamento(List<String[]> itensCarrinho) {
        this.itensCarrinho = itensCarrinho;
        inicializarComponentes(itensCarrinho);
    }

    private void inicializarComponentes(List<String[]> itensCarrinho) {
        setTitle("Tela de Pagamento");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        painel = new JPanel(null);
        painel.setBackground(Color.BLACK);

        labelTitulo = new JLabel("Pagamento", SwingConstants.CENTER);
        labelTitulo.setForeground(Color.YELLOW);
        labelTitulo.setBounds(0, 10, 600, 25);
        painel.add(labelTitulo);

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Produto", "Item", "Descrição", "Preço"}
        );

        for (String[] item : itensCarrinho) {
            model.addRow(item);
        }

        tabela = new JTable(model);
        tabela.setBackground(Color.BLACK);
        tabela.setForeground(Color.YELLOW);
        tabela.setSelectionBackground(Color.YELLOW);
        tabela.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(Color.BLACK);
        renderer.setForeground(Color.YELLOW);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        scrollPane = new JScrollPane(tabela);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBounds(50, 50, 500, 150);
        painel.add(scrollPane);

        labelFormaPagamento = new JLabel("Forma de Pagamento:");
        labelFormaPagamento.setForeground(Color.YELLOW);
        labelFormaPagamento.setBounds(50, 220, 150, 25);
        painel.add(labelFormaPagamento);

        // Obter formas de pagamento do banco de dados
        FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
        List<String> formasPagamento = formaPagamentoDAO.getFormasPagamento();
        String[] formasPagamentoArray = formasPagamento.toArray(new String[0]);

        comboPagamento = new JComboBox<>(formasPagamentoArray);
        comboPagamento.setBounds(200, 220, 150, 25);
        painel.add(comboPagamento);

        botaoFinalizar = new JButton("Finalizar");
        botaoFinalizar.setForeground(Color.YELLOW);
        botaoFinalizar.setBackground(Color.BLACK);
        botaoFinalizar.setBounds(280, 270, 100, 25);
        botaoFinalizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarCompra();
            }
        });
        painel.add(botaoFinalizar);

        add(painel);
    }

    private void finalizarCompra() {
        String pagamentoSelecionado = (String) comboPagamento.getSelectedItem();
        int idFormaPagamento = obterIdFormaPagamento(pagamentoSelecionado);

        try (Connection conn = ConexaoMySQL.conectar()) {
            String sql = "INSERT INTO vendas (id_produto, valor_compra, forma_pagamento_id) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (String[] item : itensCarrinho) {
                    try {
                        int idProduto = Integer.parseInt(item[0]);
                        BigDecimal valorCompra = new BigDecimal(item[3]);
                        pstmt.setInt(1, idProduto);
                        pstmt.setBigDecimal(2, valorCompra);
                        pstmt.setInt(3, idFormaPagamento);
                        pstmt.addBatch();
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter valores: " + item[0] + ", " + item[3]);
                        e.printStackTrace();
                    }
                }
                pstmt.executeBatch();
                JOptionPane.showMessageDialog(this, "Compra realizada com " + pagamentoSelecionado + "!");
                System.exit(0);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao finalizar compra.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.");
        }
    }

    private int obterIdFormaPagamento(String nomeFormaPagamento) {
        int idFormaPagamento = -1;
        String sql = "SELECT id FROM formas_pagamento WHERE nome = ?";
        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomeFormaPagamento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idFormaPagamento = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idFormaPagamento;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaPagamento(new ArrayList<>()).setVisible(true);
            }
        });
    }
}
