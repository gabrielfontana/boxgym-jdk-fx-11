package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.dao.MeasurementDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.InputMasks;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Measurement;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;
import org.apache.commons.validator.routines.DateValidator;
import org.controlsfx.control.PrefixSelectionComboBox;

public class MeasurementsAddController implements Initializable {

    AlertHelper ah = new AlertHelper();

    CustomerDao customerDao = new CustomerDao();
    LinkedHashMap<Integer, String> customerMap = customerDao.getCustomerForHashMap();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private PrefixSelectionComboBox<String> customerComboBox;

    @FXML
    private LimitedTextField sexTextField;

    @FXML
    private LimitedTextField ageTextField;

    @FXML
    private LimitedTextField measurementDateTextField;

    @FXML
    private LimitedTextField heightTextField;

    @FXML
    private LimitedTextField weightTextField;

    @FXML
    private AnchorPane measurementAnchorPane;

    @FXML
    private LimitedTextField neckTextField;

    @FXML
    private LimitedTextField shoulderTextField;

    @FXML
    private LimitedTextField rightArmTextField;

    @FXML
    private LimitedTextField leftArmTextField;

    @FXML
    private LimitedTextField rightForearmTextField;

    @FXML
    private LimitedTextField leftForearmTextField;

    @FXML
    private LimitedTextField thoraxTextField;

    @FXML
    private LimitedTextField waistTextField;

    @FXML
    private LimitedTextField abdomenTextField;

    @FXML
    private LimitedTextField hipTextField;

    @FXML
    private LimitedTextField rightThighTextField;

    @FXML
    private LimitedTextField leftThighTextField;

    @FXML
    private LimitedTextField rightCalfTextField;

    @FXML
    private LimitedTextField leftCalfTextField;

    @FXML
    private ImageView image;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    private String customerSex;

    private boolean created = false;

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public void initialize(URL url, ResourceBundle rb) {
        setCreated(false);
        buttonsProperties();
        
        loadCustomerNameComboBox();
        customerComboBoxListener();

        sexTextFieldListener();
        measurementChangeImage();
        masksAndRestrictions();
    }

    private void loadCustomerNameComboBox() {
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (String s : customerMap.values()) {
            obsList.add(s);
        }
        customerComboBox.setPromptText("Selecione");
        customerComboBox.setItems(obsList);
    }

    private int getKeyFromCustomerComboBox() {
        int key = 0;
        for (Map.Entry<Integer, String> entry : customerMap.entrySet()) {
            if (customerComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    private void customerComboBoxListener() {
        customerComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            CustomerDao customerSexDao = new CustomerDao();
            sexTextField.setText(customerSexDao.getCustomerSex(getKeyFromCustomerComboBox()));

            CustomerDao customerAgeDao = new CustomerDao();
            int age = Period.between(customerAgeDao.getCustomerBirthDate(getKeyFromCustomerComboBox()), LocalDate.now()).getYears();
            ageTextField.setText(String.valueOf(age));

            measurementAnchorPane.setDisable(false);
            clear();
        });
    }

    private void sexTextFieldListener() {
        sexTextField.textProperty().addListener((options, oldValue, newValue) -> {
            if (sexTextField.getText().equals("Masculino")) {
                image.setImage(new Image("/boxgym/img/m-body.jpg"));
                customerSex = "M";
            }
            if (sexTextField.getText().equals("Feminino")) {
                image.setImage(new Image("/boxgym/img/f-body.jpg"));
                customerSex = "F";
            }
        });
    }

    private void measurementChangeImage() {
        changeImageListener(neckTextField, "m-neck.jpg", "f-neck.jpg");
        changeImageListener(shoulderTextField, "m-shoulder.jpg", "f-shoulder.jpg");
        changeImageListener(rightArmTextField, "m-right-arm.jpg", "f-right-arm.jpg");
        changeImageListener(leftArmTextField, "m-left-arm.jpg", "f-left-arm.jpg");
        changeImageListener(rightForearmTextField, "m-right-forearm.jpg", "f-right-forearm.jpg");
        changeImageListener(leftForearmTextField, "m-left-forearm.jpg", "f-left-forearm.jpg");
        changeImageListener(thoraxTextField, "m-thorax.jpg", "f-thorax.jpg");
        changeImageListener(waistTextField, "m-waist.jpg", "f-waist.jpg");
        changeImageListener(abdomenTextField, "m-abdomen.jpg", "f-abdomen.jpg");
        changeImageListener(hipTextField, "m-hip.jpg", "f-hip.jpg");
        changeImageListener(rightThighTextField, "m-right-thigh.jpg", "f-right-thigh.jpg");
        changeImageListener(leftThighTextField, "m-left-thigh.jpg", "f-left-thigh.jpg");
        changeImageListener(rightCalfTextField, "m-right-calf.jpg", "f-right-calf.jpg");
        changeImageListener(leftCalfTextField, "m-left-calf.jpg", "f-left-calf.jpg");
    }

    private void changeImageListener(LimitedTextField measurement, String maleImgPath, String femaleImgPath) {
        measurement.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                if (customerSex.equals("M")) {
                    image.setImage(new Image("/boxgym/img/" + maleImgPath));
                } else if (customerSex.equals("F")) {
                    image.setImage(new Image("/boxgym/img/" + femaleImgPath));
                }
            }
        });
    }

    private void masksAndRestrictions() {
        InputMasks.dateField(measurementDateTextField);
        heightTextField.setValidationPattern("[0-9]", 3);
        InputMasks.floatField(weightTextField);
        InputMasks.floatField(neckTextField);
        InputMasks.floatField(shoulderTextField);
        InputMasks.floatField(rightArmTextField);
        InputMasks.floatField(leftArmTextField);
        InputMasks.floatField(rightForearmTextField);
        InputMasks.floatField(leftForearmTextField);
        InputMasks.floatField(thoraxTextField);
        InputMasks.floatField(waistTextField);
        InputMasks.floatField(abdomenTextField);
        InputMasks.floatField(hipTextField);
        InputMasks.floatField(rightThighTextField);
        InputMasks.floatField(leftThighTextField);
        InputMasks.floatField(rightCalfTextField);
        InputMasks.floatField(leftCalfTextField);
    }

    private Float removeCommaAndFloatParse(LimitedTextField text) {
        String measurement = "";
        Float parsedMeasurement = 0f;
        if (!text.getText().isEmpty()) {
            measurement = text.getText().replace(",", ".");
            parsedMeasurement = Float.parseFloat(measurement);
        }
        return parsedMeasurement;
    }

    @FXML
    private void save(ActionEvent event) {
        MeasurementDao measurementDao = new MeasurementDao();

        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.invalidComboBox(customerComboBox, "Selecione o campo Cliente. \n");
        validation.emptyTextField(measurementDateTextField.getText(), "Preencha o campo Data. \n");
        validation.emptyTextField(heightTextField.getText(), "Preencha o campo Altura. \n");
        validation.emptyTextField(weightTextField.getText(), "Preencha o campo Peso. \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro destas medidas", validation.getMessage());
        } else if (!DateValidator.getInstance().isValid(measurementDateTextField.getText())) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro destas medidas", "O formato do campo Data está incorreto.");
        } else {
            Measurement measurement = new Measurement(getKeyFromCustomerComboBox(),
                    LocalDate.parse(measurementDateTextField.getText().replace("/", ""), DateTimeFormatter.ofPattern("ddMMyyyy")),
                    Integer.parseInt(heightTextField.getText()), removeCommaAndFloatParse(weightTextField),
                    removeCommaAndFloatParse(neckTextField), removeCommaAndFloatParse(shoulderTextField),
                    removeCommaAndFloatParse(rightArmTextField), removeCommaAndFloatParse(leftArmTextField),
                    removeCommaAndFloatParse(rightForearmTextField), removeCommaAndFloatParse(leftForearmTextField),
                    removeCommaAndFloatParse(thoraxTextField), removeCommaAndFloatParse(waistTextField),
                    removeCommaAndFloatParse(abdomenTextField), removeCommaAndFloatParse(hipTextField),
                    removeCommaAndFloatParse(rightThighTextField), removeCommaAndFloatParse(leftThighTextField),
                    removeCommaAndFloatParse(rightCalfTextField), removeCommaAndFloatParse(leftCalfTextField));
            measurementDao.create(measurement);
            setCreated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "Medidas cadastradas com sucesso", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    @FXML
    void clear() {
        neckTextField.setText("");
        shoulderTextField.setText("");
        rightArmTextField.setText("");
        leftArmTextField.setText("");
        rightForearmTextField.setText("");
        leftForearmTextField.setText("");
        thoraxTextField.setText("");
        waistTextField.setText("");
        abdomenTextField.setText("");
        hipTextField.setText("");
        rightThighTextField.setText("");
        leftThighTextField.setText("");
        rightCalfTextField.setText("");
        leftCalfTextField.setText("");
    }
    
    private void buttonsProperties() {
        ButtonHelper.buttonCursor(saveButton, clearButton);
    }
}
