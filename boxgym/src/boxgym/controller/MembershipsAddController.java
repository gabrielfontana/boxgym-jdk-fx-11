package boxgym.controller;

import boxgym.dao.BillingDao;
import boxgym.dao.CustomerDao;
import boxgym.dao.MembershipDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Billing;
import boxgym.model.Membership;
import currencyfield.CurrencyField;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.PrefixSelectionComboBox;

public class MembershipsAddController implements Initializable {

    CustomerDao customerDao = new CustomerDao();
    LinkedHashMap<Integer, String> customerMap = customerDao.getCustomerForHashMap();
    
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
        loadCustomerNameComboBox();
        dueDateDatePicker.setEditable(false);
        loadAmountComboBox();
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
    
    private void loadAmountComboBox() {
        List<String> amountOfMonths = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        amountComboBox.setPromptText("Selecione");
        amountComboBox.setItems(FXCollections.observableArrayList(amountOfMonths));
    }

    @FXML
    void save(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.invalidComboBox(customerComboBox, "Selecione o campo Cliente. \n");
        validation.nullDatePicker(dueDateDatePicker, "Selecione o campo Data de Vencimento. \n");
        validation.invalidComboBox(amountComboBox, "Selecione o campo Quantidade. \n");
        
        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro desta(s) mensalidade(s)", validation.getMessage());
        } else {
            for (int i = 0; i < Integer.valueOf(amountComboBox.getSelectionModel().getSelectedItem()); i++) {
                LocalDate dueDate = dueDateDatePicker.getValue().plusMonths(i);
                
                Membership membership = new Membership(getKeyFromCustomerComboBox(), dueDate, new BigDecimal(priceTextField.getPrice()), "Aguardando");
                MembershipDao membershipDao = new MembershipDao();
                membershipDao.create(membership);
                
                MembershipDao getMembershipIdDao = new MembershipDao();
                int membershipId = getMembershipIdDao.getMembershipId();
                
                Billing membershipBilling = new Billing("Mensalidade " + (i + 1) + "/" + amountComboBox.getSelectionModel().getSelectedItem(), 
                        dueDate, new BigDecimal(priceTextField.getPrice()));
                membershipBilling.setFkMembership(membershipId);
                membershipBilling.setTempCustomerName(customerComboBox.getSelectionModel().getSelectedItem());

                BillingDao billingDao = new BillingDao();
                billingDao.createMembershipBilling(membershipBilling);
            }
            setCreated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "Mensalidade(s) cadastrada(s) com sucesso!", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    @FXML
    void clear(ActionEvent event) {
        customerComboBox.valueProperty().set(null);
        dueDateDatePicker.setValue(null);
        priceTextField.setPrice(0.0);
        amountComboBox.valueProperty().set(null);
    }
    
    private void buttonsProperties() {
        ButtonHelper.buttonCursor(saveButton, clearButton);
    }
    
}
