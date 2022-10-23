package boxgym.controller;

import boxgym.dao.BillingDao;
import boxgym.dao.MembershipDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Membership;
import currencyfield.CurrencyField;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.PrefixSelectionComboBox;

public class MembershipsUpdateController implements Initializable {
    
    AlertHelper ah = new AlertHelper();
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private PrefixSelectionComboBox<String> customerComboBox;
    
    @FXML
    private DatePicker dueDateDatePicker;
    
    @FXML
    private CurrencyField priceTextField;
    
    @FXML
    private PrefixSelectionComboBox<String> amountComboBox;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button clearButton;
    
    private Membership loadMembership;

    private boolean updated = false;

    public Membership getLoadMembership() {
        return loadMembership;
    }

    public void setLoadMembership(Membership loadMembership) {
        this.loadMembership = loadMembership;
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
        dueDateDatePicker.setEditable(false);
        Platform.runLater(() -> {
            initMembership();
        });
    }
    
    private void buttonsProperties() {
        ButtonHelper.buttonCursor(saveButton, clearButton);
    }
    
    private void initMembership() {
        customerComboBox.valueProperty().set(loadMembership.getTempCustomerName());
        dueDateDatePicker.setValue(loadMembership.getDueDate());
        priceTextField.setPrice(loadMembership.getPrice().doubleValue());
    }

    @FXML
    void save(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.nullDatePicker(dueDateDatePicker, "Selecione o campo Data de Vencimento. \n");
        
        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível atualizar o cadastro desta mensalidade", validation.getMessage());
        } else {
            Membership membership = new Membership(loadMembership.getMembershipId(), dueDateDatePicker.getValue(), new BigDecimal(priceTextField.getPrice()));
            
            MembershipDao membershipDao = new MembershipDao();
            membershipDao.update(membership);
            
            BillingDao billingDao = new BillingDao();
            billingDao.update(membership);
            
            setUpdated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "Mensalidade atualizada com sucesso!", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    @FXML
    void clear(ActionEvent event) {
        dueDateDatePicker.setValue(null);
        priceTextField.setPrice(0.0);
    }
    
}
