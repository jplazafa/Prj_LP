import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import javax.imageio.ImageIO;

public class TelaLogin extends JFrame {

    private JPanel painel;
    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JButton botaoRegistrar;
    private JLabel labelNome;
    private JLabel labelEmail;
    private JLabel labelSenha;
    private JLabel labelImagemFundo; // Para exibir a imagem de fundo

    private boolean registroPendente = false;

    public TelaLogin() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Configurar propriedades do frame
        setTitle("Tela de Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar painel
        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Carregar e desenhar imagem de fundo
                try {
                    Image imagemFundo = ImageIO.read(new File("C:\\Users\\Pichau\\IdeaProjects\\LP\\imagens\\Logo_da_Rolex.png"));
                    g.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        painel.setLayout(null);

        // Inicializar componentes
        labelNome = new JLabel("Nome:");
        labelNome.setBounds(70, 30, 80, 25);
        labelNome.setForeground(Color.BLACK);
        labelNome.setVisible(false); // Inicia oculto
        painel.add(labelNome);

        campoNome = new JTextField();
        campoNome.setBounds(150, 30, 180, 25);
        campoNome.setForeground(Color.BLACK);
        campoNome.setBackground(Color.WHITE);
        campoNome.setVisible(false); // Inicia oculto
        painel.add(campoNome);

        labelEmail = new JLabel("Email:");
        labelEmail.setBounds(70, 70, 80, 25);
        labelEmail.setForeground(Color.BLACK);
        painel.add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(150, 70, 180, 25);
        campoEmail.setForeground(Color.BLACK);
        campoEmail.setBackground(Color.WHITE);
        painel.add(campoEmail);

        labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(70, 110, 80, 25);
        labelSenha.setForeground(Color.BLACK);
        painel.add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(150, 110, 180, 25);
        campoSenha.setForeground(Color.BLACK);
        campoSenha.setBackground(Color.WHITE);
        painel.add(campoSenha);

        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBounds(70, 150, 100, 30);
        botaoEntrar.setForeground(Color.BLACK);
        botaoEntrar.setBackground(Color.WHITE);
        botaoEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarCredenciais();
            }
        });
        painel.add(botaoEntrar);

        botaoRegistrar = new JButton("Registrar");
        botaoRegistrar.setBounds(230, 150, 100, 30);
        botaoRegistrar.setForeground(Color.BLACK);
        botaoRegistrar.setBackground(Color.WHITE);
        botaoRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (registroPendente) {
                    registrarUsuario();
                } else {
                    solicitarNome();
                }
            }
        });
        painel.add(botaoRegistrar);

        // Adicionar label para imagem de fundo
        labelImagemFundo = new JLabel();
        labelImagemFundo.setBounds(0, 0, 400, 300); // Ajuste conforme o tamanho da tela
        painel.add(labelImagemFundo);

        // Adicionar painel ao frame
        add(painel);
    }

    private void verificarCredenciais() {
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        // Conexão com o banco de dados
        try (Connection conexao = ConexaoMySQL.conectar()) {
            String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, senha);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Credenciais válidas
                        JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                        dispose(); // Fechar a tela de login

                        // Abrir a tela principal
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new TelaPrincipal().setVisible(true);
                            }
                        });

                    } else {
                        // Credenciais inválidas
                        JOptionPane.showMessageDialog(this, "Credenciais inválidas! Por favor, clique em Registrar.");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
        }
    }

    private void solicitarNome() {
        campoNome.setVisible(true);
        labelNome.setVisible(true);
        botaoRegistrar.setText("Confirmar Registro");
        registroPendente = true;
    }

    private void registrarUsuario() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        // Conexão com o banco de dados
        try (Connection conexao = ConexaoMySQL.conectar()) {
            String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, nome);
                ps.setString(2, email);
                ps.setString(3, senha);
                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Usuário registrado com sucesso!");
                    campoNome.setText("");
                    campoEmail.setText("");
                    campoSenha.setText("");
                    campoNome.setVisible(false);
                    labelNome.setVisible(false);
                    botaoRegistrar.setText("Registrar");
                    registroPendente = false;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar o usuário: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }
}
