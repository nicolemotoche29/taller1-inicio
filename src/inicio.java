import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class inicio extends JDialog {

    private JTextField emailTF;
    private JPasswordField passwordTF;
    private JButton OKButton;
    private JButton CANCELARButton;
    private JPanel inicioPanel;
    public usuario Usuario;

    public inicio(JFrame parent) {
        //de quien estoy heredando de JFrame
        super(parent);
        setTitle("Inicio de Sesion");
        setContentPane(inicioPanel);
        setMinimumSize(new Dimension(640, 480));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        //BOTON OK
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTF.getText();
                String password= String.valueOf(passwordTF.getPassword ());
                System.out.println("Ok boton");
                Usuario = getAutenticationUser(email, password);

                if (Usuario != null) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            inicio.this, "El email o el Password estan incorrectos",
                            "intenta nuevamente",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            }
        });

        //cancelar boton
        CANCELARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("boton cancel");
                dispose();
            }

        });
    }  
        private usuario getAutenticationUser (String email, String password){
            usuario Usuario = null;
            final String DB_URL = "jdbc: mysql: // localhost/ taller1?serverTimezone=UTC";
            final String USERNAME = "ROOT";
            final String PASSWORD = " ";

            try {
                Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement();
                String sql = "SELECT * FROM users WHERE email = ? AND password=?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                System.out.println("conexion ok");

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Usuario = new usuario();
                    Usuario.id = resultSet.getInt("ID");
                    Usuario.nombre = resultSet.getString("NOMBRE");
                    Usuario.celular = resultSet.getString("CELULAR");
                    Usuario.direccion = resultSet.getString("DIRECCION");
                    Usuario.email = resultSet.getString("EMAIL");
                    Usuario.password = resultSet.getString("PASSWORD");

                }
                stmt.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return Usuario;
        }
        public static void main (String[] args){
            inicio Inicio = new inicio(null);
            usuario Usuario = Inicio.Usuario;

            if (Usuario != null) {
                System.out.println("ID" + Usuario.id);
                System.out.println("Autenticacion Correcta" + Usuario.nombre);
                System.out.println("Celular" + Usuario.celular);
                System.out.println("Direccion" + Usuario.direccion);
                System.out.println("Email" + Usuario.email);
                System.out.println("Clave" + Usuario.password);
            } else {
                System.out.println("Autenticacion fallida");
            }
        }
}

