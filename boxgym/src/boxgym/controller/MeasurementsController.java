package boxgym.controller;

import boxgym.helper.DateMask;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import limitedtextfield.LimitedTextField;
import org.apache.commons.validator.routines.FloatValidator;
import org.apache.commons.validator.routines.DateValidator;

public class MeasurementsController implements Initializable {

    @FXML
    private TextField text0;

    @FXML
    private LimitedTextField text1;

    @FXML
    private LimitedTextField text2;

    @FXML
    private LimitedTextField text3;

    @FXML
    private LimitedTextField text4;

    @FXML
    private LimitedTextField text5;

    @FXML
    private LimitedTextField text6;

    @FXML
    private LimitedTextField text7;

    @FXML
    private LimitedTextField teste;

    @FXML
    private LimitedTextField data;

    @FXML
    private ImageView image;

    private String sex;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sexTextListener();
        listeners();
        restriction();
        DateMask.dateField(data);
    }

    @FXML
    void male(ActionEvent event) {
        text0.setText("Masculino");
        enable();
    }

    @FXML
    void female(ActionEvent event) {
        text0.setText("Feminino");
        enable();
    }

    @FXML
    void data(ActionEvent event) {
        if (DateValidator.getInstance().isValid(data.getText())){
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    @FXML
    void teste(ActionEvent event) {
        if (FloatValidator.getInstance().isValid(teste.getText())) {
            System.out.println("true");
            Float var = Float.parseFloat(teste.getText());
            System.out.println(var + "\n");
        } else {
            System.out.println("false");
        }
    }

    private void restriction() {
        teste.setValidationPattern("[0-9.]", 10);
    }

    private void sexTextListener() {
        text0.textProperty().addListener((options, oldValue, newValue) -> {
            if (text0.getText().equals("Masculino")) {
                image.setImage(new Image("/boxgym/img/m-body.jpg"));
                sex = "M";
            }
            if (text0.getText().equals("Feminino")) {
                image.setImage(new Image("/boxgym/img/f-body.jpg"));
                sex = "F";
            }
        });
    }

    private void listeners() {
        changeListener(text1, "m-neck.jpg", "f-neck.jpg");
        changeListener(text2, "m-shoulder.jpg", "f-shoulder.jpg");
        changeListener(text3, "m-right-arm.jpg", "f-right-arm.jpg");
        changeListener(text4, "m-left-arm.jpg", "f-left-arm.jpg");
        changeListener(text5, "m-right-forearm.jpg", "f-right-forearm.jpg");
        changeListener(text6, "m-left-forearm.jpg", "f-left-forearm.jpg");
        changeListener(text7, "m-thorax.jpg", "f-thorax.jpg");
    }

    private void changeListener(LimitedTextField neck, String maleImgPath, String femaleImgPath) {
        neck.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                if (sex.equals("M")) {
                    image.setImage(new Image("/boxgym/img/" + maleImgPath));
                } else if (sex.equals("F")) {
                    image.setImage(new Image("/boxgym/img/" + femaleImgPath));
                }
            }
        });
    }

    private void enable() {
        text1.setDisable(false);
        text2.setDisable(false);
        text3.setDisable(false);
        text4.setDisable(false);
        text5.setDisable(false);
        text6.setDisable(false);
        text7.setDisable(false);
    }
}
