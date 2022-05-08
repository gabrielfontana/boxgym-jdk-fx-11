package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.model.Customer;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class CustomersController implements Initializable {

     AlertHelper alert = new AlertHelper();

    private Customer selected;
    
    @FXML
    private MenuButton filterButton;

    @FXML
    private CheckMenuItem caseSensitiveOp;

    @FXML
    private ToggleGroup filterOptions;

    @FXML
    private RadioMenuItem containsOp;

    @FXML
    private RadioMenuItem alphabeticalEqualsToOp;

    @FXML
    private RadioMenuItem startsWithOp;

    @FXML
    private RadioMenuItem endsWithOp;

    @FXML
    private TextField searchBox;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private MenuButton exportButton;
    
    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, Integer> customerIdTableColumn;

    @FXML
    private TableColumn<Customer, String> personRegistryTableColumn;

    @FXML
    private TableColumn<Customer, String> nameTableColumn;

    @FXML
    private TableColumn<Customer, String> sexTableColumn;
    
    @FXML
    private TableColumn<Customer, String> emailTableColumn;

    @FXML
    private TableColumn<Customer, String> phoneTableColumn;

    @FXML
    private TableColumn<Customer, String> addressTableColumn;

    @FXML
    private TableColumn<Customer, String> cityTableColumn;

    @FXML
    private TableColumn<Customer, String> federativeUnitTableColumn;
    
    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Label customerIdLabel;

    @FXML
    private Label personRegistryLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label sexLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label zipCodeLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label addressComplementLabel;

    @FXML
    private Label districtLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label federativeUnitLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label updatedAtLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(exportButton, filterButton, addButton, updateButton, deleteButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initCustomerTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }    
    
    @FXML
    void addCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/CustomersAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            CustomersAddController controller = loader.getController();

            StageHelper.createAddOrUpdateStage("Adicionando Cliente", root);

            if (controller.isCreated()) {
                refreshTableView();
                customerTableView.getSelectionModel().selectLast();
            }
        } catch (IOException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void updateCustomer(ActionEvent event) {
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um cliente para editar!", "");
        } else {
            int index = customerTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/CustomersUpdate.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                CustomersUpdateController controller = loader.getController();
                controller.setLoadCustomer(selected);

                StageHelper.createAddOrUpdateStage("Editando Cliente", root);

                if (controller.isUpdated()) {
                    refreshTableView();
                    customerTableView.getSelectionModel().select(index);
                }
            } catch (IOException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void deleteCustomer(ActionEvent event) {
        CustomerDao customerDao = new CustomerDao();

        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um cliente para excluir!", "");
        } else {
            alert.confirmationAlert("Tem certeza que deseja excluir \n o cliente '" + selected.getName()+ "'?", "Esta ação é irreversível!");
            if (alert.getResult().get() == ButtonType.YES) {
                customerDao.delete(selected);
                refreshTableView();
                resetDetails();
                alert.customAlert(Alert.AlertType.WARNING, "O cliente foi excluído com sucesso!", "");
            }
        }
    }
    
    private void resetDetails() {
        if (selected == null) {
            customerIdLabel.setText("");
            personRegistryLabel.setText("");
            nameLabel.setText("");
            sexLabel.setText("");
            emailLabel.setText("");
            phoneLabel.setText("");
            zipCodeLabel.setText("");
            addressLabel.setText("");
            addressComplementLabel.setText("");
            districtLabel.setText("");
            cityLabel.setText("");
            federativeUnitLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            customerIdLabel.setText(String.valueOf(selected.getCustomerId()));
            personRegistryLabel.setText(selected.getPersonRegistry());
            nameLabel.setText(selected.getName());
            sexLabel.setText(selected.getSex());
            emailLabel.setText(selected.getEmail());
            phoneLabel.setText(selected.getPhone());
            zipCodeLabel.setText(selected.getZipCode());
            addressLabel.setText(selected.getAddress());
            addressComplementLabel.setText(selected.getAddressComplement());
            districtLabel.setText(selected.getDistrict());
            cityLabel.setText(selected.getCity());
            federativeUnitLabel.setText(selected.getFederativeUnit());
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }
    
    private ObservableList<Customer> loadData() {
        CustomerDao customerDao = new CustomerDao();
        return FXCollections.observableArrayList(customerDao.read());
    }

    private void refreshTableView() {
        customerTableView.setItems(loadData());
        search();
        countLabel.setText(TableViewCount.footerMessage(customerTableView.getItems().size(), "resultado"));
    }

    private void initCustomerTableView() {
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory("customerId"));
        personRegistryTableColumn.setCellValueFactory(new PropertyValueFactory("personRegistry"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        sexTableColumn.setCellValueFactory(new PropertyValueFactory("sex"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory("email"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory("phone"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory("address"));
        cityTableColumn.setCellValueFactory(new PropertyValueFactory("city"));
        federativeUnitTableColumn.setCellValueFactory(new PropertyValueFactory("federativeUnit"));
        refreshTableView();
    }
    
    private void search() {
        
    }
    
    private void listeners() {
        customerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Customer) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(customerTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
            }
        });
        
        caseSensitiveOp.selectedProperty().addListener((observable, oldValue, newValue) -> {
            searchBox.setText("");
            searchBox.requestFocus();
        });
        
        filterOptions.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            searchBox.setText("");
            searchBox.requestFocus();
        });
    }

    @FXML
    void exportToExcel(ActionEvent event) {

    }

    @FXML
    void generatePdf(ActionEvent event) {

    }

    @FXML
    void goToFirstRow(MouseEvent event) {
        customerTableView.scrollTo(0);
        customerTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
         if (customerTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            customerTableView.scrollTo(customerTableView.getItems().size() - 1);
            customerTableView.getSelectionModel().selectLast(); 
         }
    }
    
}
