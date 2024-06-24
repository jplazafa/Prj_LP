import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginScreen extends JFrame implements ActionListener {
    private JTextField emailField;
    private JPasswordField passwordField;
    private Connection connection;

    public LoginScreen() {
        setTitle("Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel emailLabel = new JLabel("Email:");
        panel.add(emailLabel);
        emailField = new JTextField();
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Senha:");
        panel.add(passwordLabel);
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        panel.add(loginButton);

        add(panel);
        setVisible(true);
        connectToDatabase();
    }
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/";
            String username = "root";
            String password = "1234";
            System.out.println("Entrei aqui");
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.");
            System.exit(1);
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Login")) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try {
                // Consulta SQL para verificar as credenciais do usuário
                String sql = "SELECT * FROM usuarios WHERE email=? AND senha=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                    // Adicione aqui a lógica para abrir outra janela após o login bem-sucedido
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciais inválidas.");
                }

                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao verificar as credenciais.");
            }
        }
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}