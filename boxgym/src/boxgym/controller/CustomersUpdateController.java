package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;
import org.controlsfx.control.PrefixSelectionComboBox;

public class CustomersUpdateController implements Initializable {

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

    private Customer loadCustomer;

    private boolean updated = false;

    public Customer getLoadCustomer() {
        return loadCustomer;
    }

    public void setLoadCustomer(Customer loadCustomer) {
        this.loadCustomer = loadCustomer;
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
        loadSexComboBox();
        loadFederativeUnitComboBox();
        customersInputRestrictions();

        Platform.runLater(() -> {
            initCustomer();
        });
    }

    private void buttonsProperties() {
        ButtonHelper.buttonCursor(saveButton, clearButton);
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
        nameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ]", 255);
        emailTextField.setValidationPattern("[A-Za-z0-9@._-]", 255);
        phoneTextField.setValidationPattern("[0-9]", 11, "Sem pontuação!");
        zipCodeTextField.setValidationPattern("[0-9]", 8, "Sem pontuação!");
        addressTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 .,_-]", 255);
        addressComplementTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 .,_-]", 255);
        districtTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF ]", 255);
        cityTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF '-]", 255);
    }

    private void initCustomer() {
        personRegistryTextField.setText(loadCustomer.getPersonRegistry());
        nameTextField.setText(loadCustomer.getName());
        sexComboBox.valueProperty().set(loadCustomer.getSex());
        emailTextField.setText(loadCustomer.getEmail());
        phoneTextField.setText(loadCustomer.getPhone());
        zipCodeTextField.setText(loadCustomer.getZipCode());
        addressTextField.setText(loadCustomer.getAddress());
        addressComplementTextField.setText(loadCustomer.getAddressComplement());
        districtTextField.setText(loadCustomer.getDistrict());
        cityTextField.setText(loadCustomer.getCity());
        federativeUnitComboBox.valueProperty().set(loadCustomer.getFederativeUnit());
    }

    @FXML
    void save(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.emptyTextField(nameTextField.getText(), "O campo 'Nome' está vazio! \n");
        validation.invalidComboBox(sexComboBox, "O campo 'Sexo' está vazio! \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível atualizar o cadastro deste cliente!", validation.getMessage());
        } else if (!(phoneTextField.getText().length() == 0 || phoneTextField.getText().length() == 10 || phoneTextField.getText().length() == 11)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível atualizar o cadastro deste cliente!", "O formato do campo 'Telefone' está incorreto.");
        } else if (!(zipCodeTextField.getText().length() == 0 || zipCodeTextField.getText().length() == 8)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível atualizar o cadastro deste cliente!", "O campo 'CEP' deve conter 8 dígitos.");
        } else {
            String selectedFederativeUnit = federativeUnitComboBox.getSelectionModel().getSelectedItem();
            if (selectedFederativeUnit == null) {
                selectedFederativeUnit = "";
            }
            Customer customer = new Customer(loadCustomer.getCustomerId(), personRegistryTextField.getText(), nameTextField.getText(), sexComboBox.getSelectionModel().getSelectedItem(),
                    emailTextField.getText(), phoneTextField.getText(), zipCodeTextField.getText(), addressTextField.getText(), addressComplementTextField.getText(),
                    districtTextField.getText(), cityTextField.getText(), selectedFederativeUnit);
            CustomerDao customerDao = new CustomerDao();
            customerDao.update(customer);
            setUpdated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "O cliente foi atualizado com sucesso!", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    @FXML
    void clear(ActionEvent event) {
        nameTextField.setText("");
        sexComboBox.valueProperty().set(null);
        emailTextField.setText("");
        phoneTextField.setText("");
        zipCodeTextField.setText("");
        addressTextField.setText("");
        addressComplementTextField.setText("");
        districtTextField.setText("");
        cityTextField.setText("");
        federativeUnitComboBox.valueProperty().set(null);
    }

}
