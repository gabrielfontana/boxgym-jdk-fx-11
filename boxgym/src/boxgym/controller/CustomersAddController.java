package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.CpfValidator;
import boxgym.helper.InputMasks;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Customer;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;
import org.apache.commons.validator.routines.DateValidator;
import org.controlsfx.control.PrefixSelectionComboBox;

public class CustomersAddController implements Initializable {

    AlertHelper ah = new AlertHelper();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private LimitedTextField personRegistryTextField;

    @FXML
    private LimitedTextField nameTextField;

    @FXML
    private PrefixSelectionComboBox<String> sexComboBox;
    
    @FXML
    private LimitedTextField birthDateTextField;

    @FXML
    private LimitedTextField emailTextField;

    @FXML
    private LimitedTextField phoneTextField;

    @FXML
    private LimitedTextField zipCodeTextField;

    @FXML
    private LimitedTextField addressTextField;

    @FXML
    private LimitedTextField addressComplementTextField;

    @FXML
    private LimitedTextField districtTextField;

    @FXML
    private LimitedTextField cityTextField;

    @FXML
    private PrefixSelectionComboBox<String> federativeUnitComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    private boolean created = false;

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCreated(false);
        buttonsProperties();
        loadSexComboBox();
        loadFederativeUnitComboBox();
        customersInputRestrictions();
    }

    private void loadSexComboBox() {
        String[] maleAndFemale = {"Masculino", "Feminino"};
        sexComboBox.setPromptText("Selecione");
        sexComboBox.setItems(FXCollections.observableArrayList(maleAndFemale));
    }

    private void loadFederativeUnitComboBox() {
        String[] federativeUnitsList = {
            "AC", "AL", "AP", "AM", "BA", "CE",
            "DF", "ES", "GO", "MA", "MT", "MS",
            "MG", "PA", "PB", "PR", "PE", "PI",
            "RJ", "RN", "RS", "RO", "RR", "SC",
            "SP", "SE", "TO"
        };
        federativeUnitComboBox.setPromptText("Selecione");
        federativeUnitComboBox.setItems(FXCollections.observableArrayList(federativeUnitsList));
    }

    private void customersInputRestrictions() {
        personRegistryTextField.setValidationPattern("[0-9]", 11, "Sem pontuação!");
        nameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ]", 255);
        InputMasks.dateField(birthDateTextField);
        emailTextField.setValidationPattern("[A-Za-z0-9@._-]", 255);
        phoneTextField.setValidationPattern("[0-9]", 11, "Sem pontuação!");
        zipCodeTextField.setValidationPattern("[0-9]", 8, "Sem pontuação!");
        addressTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 .,_-]", 255);
        addressComplementTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 .,_-]", 255);
        districtTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF ]", 255);
        cityTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF '-]", 255);
    }

    @FXML
    void save(ActionEvent event) {
        CustomerDao customerDao = new CustomerDao();

        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.emptyTextField(personRegistryTextField.getText(), "Preencha o campo CPF. \n");
        validation.emptyTextField(nameTextField.getText(), "Preencha o campo Nome. \n");
        validation.invalidComboBox(sexComboBox, "Selecione o campo Sexo. \n");
        validation.emptyTextField(birthDateTextField.getText(), "Preencha o campo Data de Nascimento. \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste(a) cliente", validation.getMessage());
        } else if (!(CpfValidator.isValid(personRegistryTextField.getText()))) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste(a) cliente", "O campo CPF está inválido.");
        } else if (customerDao.checkExistingCustomer(personRegistryTextField.getText())) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste(a) cliente", "Este CPF já está cadastrado no sistema.");
            personRegistryTextField.setText("");
        } else if (!DateValidator.getInstance().isValid(birthDateTextField.getText())){
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste(a) cliente", "O formato do campo Data de Nascimento está incorreto.");
        } else if (!(phoneTextField.getText().length() == 0 || phoneTextField.getText().length() == 10 || phoneTextField.getText().length() == 11)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste(a) cliente", "O formato do campo Telefone está incorreto.");
        } else if (!(zipCodeTextField.getText().length() == 0 || zipCodeTextField.getText().length() == 8)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste(a) cliente", "O formato do campo CEP está incorreto.");
        } else {
            String selectedFederativeUnit = federativeUnitComboBox.getSelectionModel().getSelectedItem();
            if (selectedFederativeUnit == null) {
                selectedFederativeUnit = "";
            }
            Customer customer = new Customer(personRegistryTextField.getText(), nameTextField.getText(), sexComboBox.getSelectionModel().getSelectedItem(),
                    LocalDate.parse(birthDateTextField.getText().replace("/", ""), DateTimeFormatter.ofPattern("ddMMyyyy")), emailTextField.getText(), phoneTextField.getText(), zipCodeTextField.getText(), addressTextField.getText(), addressComplementTextField.getText(),
                    districtTextField.getText(), cityTextField.getText(), selectedFederativeUnit);
            customerDao.create(customer);
            setCreated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "Cliente cadastrado(a) com sucesso", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    @FXML
    void clear(ActionEvent event) {
        personRegistryTextField.setText("");
        nameTextField.setText("");
        sexComboBox.valueProperty().set(null);
        birthDateTextField.setText("");
        emailTextField.setText("");
        phoneTextField.setText("");
        zipCodeTextField.setText("");
        addressTextField.setText("");
        addressComplementTextField.setText("");
        districtTextField.setText("");
        cityTextField.setText("");
        federativeUnitComboBox.valueProperty().set(null);
    }

    private void buttonsProperties() {
        ButtonHelper.buttonCursor(saveButton, clearButton);
    }

}
