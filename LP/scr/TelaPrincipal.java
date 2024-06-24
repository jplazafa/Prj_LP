import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private JPanel painel;
    private JTable tabelaItens;
    private JScrollPane scrollPane;
    private JLabel labelTitulo;
    private JPanel painelBotoes;
    private JButton botaoCarrinho;
    private JButton botaoAdicionarCarrinho;
    private List<String[]> carrinho;

    public TelaPrincipal() {
        carrinho = new ArrayList<>();
        inicializarComponentes();
        carregarDadosDoBanco();
    }

    private void inicializarComponentes() {
        setTitle("Tela Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.BLACK);

        labelTitulo = new JLabel("Itens Cadastrados", SwingConstants.CENTER);
        labelTitulo.setForeground(Color.ORANGE);
        labelTitulo.setPreferredSize(new Dimension(600, 25));
        painel.add(labelTitulo, BorderLayout.NORTH);

        tabelaItens = new JTable();
        tabelaItens.setForeground(Color.ORANGE);
        tabelaItens.setBackground(Color.BLACK);
        tabelaItens.setPreferredScrollableViewportSize(new Dimension(580, 300));
        tabelaItens.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(tabelaItens);
        scrollPane.setBackground(Color.BLACK);
        painel.add(scrollPane, BorderLayout.CENTER);

        tabelaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabelaItens.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaItens.getSelectedRow();
                if (linhaSelecionada != -1) {
                    String id = (String) tabelaItens.getValueAt(linhaSelecionada, 0);
                    String item = (String) tabelaItens.getValueAt(linhaSelecionada, 1);
                    String descricao = (String) tabelaItens.getValueAt(linhaSelecionada, 2);
                    String preco = (String) tabelaItens.getValueAt(linhaSelecionada, 3);
                }
            }
        });

        painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.setBackground(Color.BLACK);

        botaoAdicionarCarrinho = new JButton("Adicionar");
        botaoAdicionarCarrinho.setPreferredSize(new Dimension(120, 30));
        botaoAdicionarCarrinho.setForeground(Color.ORANGE);
        botaoAdicionarCarrinho.setBackground(Color.BLACK);
        botaoAdicionarCarrinho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarAoCarrinho();
            }
        });
        painelBotoes.add(botaoAdicionarCarrinho);

        botaoCarrinho = new JButton("Carrinho");
        botaoCarrinho.setPreferredSize(new Dimension(120, 30));
        botaoCarrinho.setForeground(Color.ORANGE);
        botaoCarrinho.setBackground(Color.BLACK);
        botaoCarrinho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCarrinho();
            }
        });
        painelBotoes.add(botaoCarrinho);

        painel.add(painelBotoes, BorderLayout.SOUTH);
        add(painel);

        personalizarCabecalhoTabela();
    }

    private void personalizarCabecalhoTabela() {
        JTableHeader cabecalho = tabelaItens.getTableHeader();
        cabecalho.setForeground(Color.ORANGE);
        cabecalho.setBackground(Color.BLACK);
        cabecalho.setFont(new Font("Arial", Font.BOLD, 12));
        cabecalho.setPreferredSize(new Dimension(580, 30));
        cabecalho.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) cabecalho.getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void carregarDadosDoBanco() {
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conexao = ConexaoMySQL.conectar();
            String sql = "SELECT id, nome, descricao, preco FROM produtos";
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Item", "Descrição", "Preço"}, 0);

            while (rs.next()) {
                String id = rs.getString("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String preco = rs.getString("preco");
                model.addRow(new String[]{id, nome, descricao, preco});
            }

            tabelaItens.setModel(model);

            tabelaItens.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
            tabelaItens.getColumnModel().getColumn(1).setPreferredWidth(120); // Item
            tabelaItens.getColumnModel().getColumn(2).setPreferredWidth(300); // Descrição
            tabelaItens.getColumnModel().getColumn(3).setPreferredWidth(80); // Preço

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do banco de dados.");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ConexaoMySQL.fecharConexao(conexao);
        }
    }

    private void adicionarAoCarrinho() {
        int linhaSelecionada = tabelaItens.getSelectedRow();
        if (linhaSelecionada != -1) {
            DefaultTableModel model = (DefaultTableModel) tabelaItens.getModel();
            String id = (String) model.getValueAt(linhaSelecionada, 0);
            String item = (String) model.getValueAt(linhaSelecionada, 1);
            String descricao = (String) model.getValueAt(linhaSelecionada, 2);
            String preco = (String) model.getValueAt(linhaSelecionada, 3);
            carrinho.add(new String[]{id, item, descricao, preco});
            JOptionPane.showMessageDialog(this, "Item adicionado ao carrinho!");
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela.");
        }
    }

    private void abrirCarrinho() {
        Carrinho carrinhoTela = new Carrinho(carrinho);
        carrinhoTela.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }
}
