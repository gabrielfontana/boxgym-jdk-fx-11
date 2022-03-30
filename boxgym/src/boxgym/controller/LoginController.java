package boxgym.controller;

import boxgym.helper.AlertHelper;
import boxgym.dao.UserDao;
import boxgym.helper.ButtonHelper;
import boxgym.helper.StageHelper;
import boxgym.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;

public class LoginController implements Initializable {
    
    AlertHelper ah = new AlertHelper();
    
    @FXML
    private AnchorPane content;

    @FXML
    private LimitedTextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ButtonHelper.loginButtons(loginButton, registerLabel);
        registerInputRestrictions();
    }
    
    public void registerInputRestrictions() {
        usernameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 32);
    }

    @FXML
    void login(ActionEvent event) throws IOException {
        if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível efetuar o login!", "Por favor, preencha todos os campos!");
        } else {
            String passwordSha256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(passwordTextField.getText());

            User user = new User(usernameTextField.getText(), passwordSha256);
            UserDao userDao = new UserDao();

            if (userDao.authenticate(user)) {
                loginButton.getScene().getWindow().hide();
                StageHelper sh = new StageHelper();
                sh.createMainScreenStage("/boxgym/view/MainScreen.fxml", "Tela Principal");
            } else {
                ah.customAlert(Alert.AlertType.WARNING, "Não foi possível efetuar o login!", "Usuário e/ou senha inválido(s)!");
                usernameTextField.setText("");
                passwordTextField.setText("");
            }
        }
    }

    @FXML
    void register(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/boxgym/view/Register.fxml"));
        content.getChildren().removeAll();
        content.getChildren().setAll(fxml);
        boxgym.Main.stage.setTitle("Cadastro");
    }
}
