import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Carrinho extends JFrame {

    private JPanel painel;
    private JTable tabela;
    private JScrollPane scrollPane;
    private JLabel labelTitulo;
    private JButton botaoVoltar;
    private JButton botaoPagamento;
    private List<String[]> itensCarrinho;

    public Carrinho(List<String[]> itensCarrinho) {
        this.itensCarrinho = itensCarrinho;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Carrinho de Compras");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.BLACK);

        labelTitulo = new JLabel("Carrinho", SwingConstants.CENTER);
        labelTitulo.setForeground(Color.YELLOW);
        labelTitulo.setPreferredSize(new Dimension(600, 25));
        painel.add(labelTitulo, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Produto", "Item", "Descrição", "Preço"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

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
        painel.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setBackground(Color.BLACK);

        botaoVoltar = new JButton("Voltar");
        botaoVoltar.setForeground(Color.YELLOW);
        botaoVoltar.setBackground(Color.BLACK);
        botaoVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        painelBotoes.add(botaoVoltar);

        botaoPagamento = new JButton("Pagamento");
        botaoPagamento.setForeground(Color.YELLOW);
        botaoPagamento.setBackground(Color.BLACK);
        botaoPagamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaPagamento();
            }
        });
        painelBotoes.add(botaoPagamento);

        painel.add(painelBotoes, BorderLayout.SOUTH);
        add(painel);

        personalizarCabecalhoTabela();
    }

    private void personalizarCabecalhoTabela() {
        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setForeground(Color.YELLOW);
        cabecalho.setBackground(Color.BLACK);
        cabecalho.setFont(new Font("Arial", Font.BOLD, 12));
        cabecalho.setPreferredSize(new Dimension(600, 30));
        cabecalho.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) cabecalho.getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void abrirTelaPagamento() {
        TelaPagamento telaPagamento = new TelaPagamento(itensCarrinho);
        telaPagamento.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Carrinho(null).setVisible(true);
        });
    }
}
