package boxgym.jdbc;

import boxgym.helper.AlertHelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class ConnectionFactory {
    
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/boxgym";
    private static final String CONNECTION_USERNAME = "root";
    private static final String CONNECTION_USERPASSWORD = "phpMyAdm1n$QLp@sswd";
    
    AlertHelper ah = new AlertHelper();
    
    public Connection getConnection() {
        try {
            String url = CONNECTION_URL;
            String userName = CONNECTION_USERNAME;
            String userPassword = CONNECTION_USERPASSWORD;

            return DriverManager.getConnection(url, userName, userPassword);
        } catch (SQLException ex) {
            ah.customAlert(Alert.AlertType.INFORMATION, "Ops, algo deu errado!", "Falha ao estabelecer conexão com o banco de dados.");
            throw new RuntimeException(ex);
        }
    }
}
