package boxgym.helper;

import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import jfxtras.styles.jmetro.FlatAlert;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class AlertHelper {

    private Optional<ButtonType> result;

    public Optional<ButtonType> getResult() {
        return result;
    }

    public void setResult(Optional<ButtonType> result) {
        this.result = result;
    }

    public void customAlert(Alert.AlertType type, String headerText, String contentText) {
        FlatAlert alert = new FlatAlert(type);
        
        alertCss(alert);
        
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        
        Scene scene = (Scene) alert.getDialogPane().getScene();
        JMetro jMetro = new JMetro(scene, Style.LIGHT);
        
        alert.showAndWait();
    }

    public void confirmationAlert(String headerText, String contentText) {
        FlatAlert alert = new FlatAlert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        
        alertCss(alert);
        
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        yesButton.setDefaultButton(false);

        Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
        noButton.setDefaultButton(true);

        Scene scene = (Scene) alert.getDialogPane().getScene();
        JMetro jMetro = new JMetro(scene, Style.LIGHT);

        result = alert.showAndWait();
    }

    private void alertCss(FlatAlert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/boxgym/css/dialog-pane.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
    }

}
