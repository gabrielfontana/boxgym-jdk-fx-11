package boxgym.controller;

import boxgym.dao.SupplierDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Supplier;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;

public class SuppliersUpdateController implements Initializable {
    
    AlertHelper ah = new AlertHelper();
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private LimitedTextField companyRegistryTextField;

    @FXML
    private LimitedTextField corporateNameTextField;

    @FXML
    private LimitedTextField tradeNameTextField;

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
    private ComboBox<String> federativeUnitComboBox;
    
    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;


    private Supplier loadSupplier;

    private boolean updated = false;

    public Supplier getLoadSupplier() {
        return loadSupplier;
    }

    public void setLoadSupplier(Supplier loadSupplier) {
        this.loadSupplier = loadSupplier;
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
        ButtonHelper.addOrUpdateButtons(saveButton, clearButton);
        loadFederativeUnitComboBox();
        suppliersInputRestrictions();

        Platform.runLater(() -> {
            initSupplier();
        });
    }

    private void initSupplier() {
        companyRegistryTextField.setText(loadSupplier.getCompanyRegistry());
        corporateNameTextField.setText(loadSupplier.getCorporateName());
        tradeNameTextField.setText(loadSupplier.getTradeName());
        emailTextField.setText(loadSupplier.getEmail());
        phoneTextField.setText(loadSupplier.getPhone());
        zipCodeTextField.setText(loadSupplier.getZipCode());
        addressTextField.setText(loadSupplier.getAddress());
        addressComplementTextField.setText(loadSupplier.getAddressComplement());
        districtTextField.setText(loadSupplier.getDistrict());
        cityTextField.setText(loadSupplier.getCity());
        federativeUnitComboBox.valueProperty().set(loadSupplier.getFederativeUnit());
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

    private void suppliersInputRestrictions() {
        corporateNameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
        tradeNameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
        emailTextField.setValidationPattern("[A-Za-z0-9@._-]", 255);
        phoneTextField.setValidationPattern("[0-9]", 11, "Sem pontuação!");
        zipCodeTextField.setValidationPattern("[0-9]", 8, "Sem pontuação!");
        addressTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
        addressComplementTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
        districtTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF ]", 255);
        cityTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF ]", 255);
    }

    @FXML
    void save(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper();
        validation.handleEmptyField(corporateNameTextField.getText(), "'Razão Social'\n");
        validation.handleEmptyField(tradeNameTextField.getText(), "'Nome Fantasia'");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível editar o cadastro deste fornecedor!", validation.getMessage());
        } else if (!(phoneTextField.getText().length() == 0 || phoneTextField.getText().length() == 10 || phoneTextField.getText().length() == 11)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste fornecedor!", "O formato do campo 'Telefone' está incorreto.");
        } else if (!(zipCodeTextField.getText().length() == 0 || zipCodeTextField.getText().length() == 8)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste fornecedor!", "O campo 'CEP' deve conter 8 dígitos.");
        } else {
            Supplier supplier = new Supplier(loadSupplier.getSupplierId(), corporateNameTextField.getText(), tradeNameTextField.getText(),
                emailTextField.getText(), phoneTextField.getText(), zipCodeTextField.getText(), addressTextField.getText(), addressComplementTextField.getText(),
                districtTextField.getText(), cityTextField.getText(), federativeUnitComboBox.getSelectionModel().getSelectedItem());
            SupplierDao supplierDao = new SupplierDao();
            supplierDao.update(supplier);
            setUpdated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "O fornecedor foi editado com sucesso!", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    @FXML
    void clear() {
        corporateNameTextField.setText("");
        tradeNameTextField.setText("");
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
