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
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class MeasurementsUpdateController implements Initializable {

    AlertHelper ah = new AlertHelper();

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

    private Measurement loadMeasurement;

    private boolean updated = false;

    public Measurement getLoadMeasurement() {
        return loadMeasurement;
    }

    public void setLoadMeasurement(Measurement loadMeasurement) {
        this.loadMeasurement = loadMeasurement;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpdated(false);
        buttonsProperties();

        sexTextFieldListener();
        measurementChangeImage();
        masksAndRestrictions();
        eraseZeroedValues();

        Platform.runLater(() -> {
            initMeasurement();
        });
    }

    private void initMeasurement() {
        customerComboBox.valueProperty().set(loadMeasurement.getTempCustomerName());

        CustomerDao customerSexDao = new CustomerDao();
        sexTextField.setText(customerSexDao.getCustomerSex(loadMeasurement.getFkCustomer()));

        CustomerDao customerAgeDao = new CustomerDao();
        int age = Period.between(customerAgeDao.getCustomerBirthDate(loadMeasurement.getFkCustomer()), LocalDate.now()).getYears();
        ageTextField.setText(String.valueOf(age));

        measurementDateTextField.setText(loadMeasurement.getMeasurementDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        heightTextField.setText(String.valueOf(loadMeasurement.getHeight()));
        weightTextField.setText(String.valueOf(loadMeasurement.getWeight()));
        neckTextField.setText(String.valueOf(loadMeasurement.getNeck()));
        shoulderTextField.setText(String.valueOf(loadMeasurement.getShoulder()));
        rightArmTextField.setText(String.valueOf(loadMeasurement.getRightArm()));
        leftArmTextField.setText(String.valueOf(loadMeasurement.getLeftArm()));
        rightForearmTextField.setText(String.valueOf(loadMeasurement.getRightForearm()));
        leftForearmTextField.setText(String.valueOf(loadMeasurement.getLeftForearm()));
        thoraxTextField.setText(String.valueOf(loadMeasurement.getThorax()));
        waistTextField.setText(String.valueOf(loadMeasurement.getWaist()));
        abdomenTextField.setText(String.valueOf(loadMeasurement.getAbdomen()));
        hipTextField.setText(String.valueOf(loadMeasurement.getHip()));
        rightThighTextField.setText(String.valueOf(loadMeasurement.getRightThigh()));
        leftThighTextField.setText(String.valueOf(loadMeasurement.getLeftThigh()));
        rightCalfTextField.setText(String.valueOf(loadMeasurement.getRightCalf()));
        leftCalfTextField.setText(String.valueOf(loadMeasurement.getLeftCalf()));
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

    private void eraseZeroedValues() {
        eraseListener(neckTextField);
        eraseListener(shoulderTextField);
        eraseListener(rightArmTextField);
        eraseListener(leftArmTextField);
        eraseListener(rightForearmTextField);
        eraseListener(leftForearmTextField);
        eraseListener(thoraxTextField);
        eraseListener(waistTextField);
        eraseListener(abdomenTextField);
        eraseListener(hipTextField);
        eraseListener(rightThighTextField);
        eraseListener(leftThighTextField);
        eraseListener(rightCalfTextField);
        eraseListener(leftCalfTextField);
    }

    private void eraseListener(LimitedTextField text) {
        text.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            Float measurementValue = removeCommaAndFloatParse(text);
            if (newValue) {
                if (measurementValue == 0f) {
                    text.setText("");
                }
            }

            if (oldValue) {
                if (measurementValue != 0f && measurementValue != null) {
                    text.setText(String.valueOf(measurementValue).replace(".", ","));
                } else {
                    text.setText("0.0");
                }
            }
        });

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
        validation.emptyTextField(measurementDateTextField.getText(), "Data inválida! \n");
        validation.emptyTextField(heightTextField.getText(), "Altura inválida! \n");
        validation.emptyTextField(weightTextField.getText(), "Peso inválido! \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível atualizar o cadastro desta medição!", validation.getMessage());
        } else if (!DateValidator.getInstance().isValid(measurementDateTextField.getText())) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível atualizar o cadastro desta medição!", "O formato do campo 'Data' está incorreto");
        } else {
            Measurement measurement = new Measurement(LocalDate.parse(measurementDateTextField.getText().replace("/", ""), DateTimeFormatter.ofPattern("ddMMyyyy")),
                    Integer.parseInt(heightTextField.getText()), removeCommaAndFloatParse(weightTextField),
                    removeCommaAndFloatParse(neckTextField), removeCommaAndFloatParse(shoulderTextField),
                    removeCommaAndFloatParse(rightArmTextField), removeCommaAndFloatParse(leftArmTextField),
                    removeCommaAndFloatParse(rightForearmTextField), removeCommaAndFloatParse(leftForearmTextField),
                    removeCommaAndFloatParse(thoraxTextField), removeCommaAndFloatParse(waistTextField),
                    removeCommaAndFloatParse(abdomenTextField), removeCommaAndFloatParse(hipTextField),
                    removeCommaAndFloatParse(rightThighTextField), removeCommaAndFloatParse(leftThighTextField),
                    removeCommaAndFloatParse(rightCalfTextField), removeCommaAndFloatParse(leftCalfTextField),
                    loadMeasurement.getMeasurementId());
            measurementDao.update(measurement);
            setUpdated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "As medidas foram atualizadas com sucesso!", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    @FXML
    private void clear(ActionEvent event) {

    }

    private void buttonsProperties() {
        ButtonHelper.buttonCursor(saveButton, clearButton);
    }
}
