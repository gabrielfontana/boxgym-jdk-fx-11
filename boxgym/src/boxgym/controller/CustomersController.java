package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.ChangeTableRow;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Customer;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, String> personRegistryTableColumn;

    @FXML
    private TableColumn<Customer, String> nameTableColumn;

    @FXML
    private TableColumn<Customer, String> sexTableColumn;

    @FXML
    private TableColumn<Customer, LocalDate> birthDateTableColumn;

    @FXML
    private TableColumn<Customer, String> emailTableColumn;

    @FXML
    private TableColumn<Customer, String> phoneTableColumn;

    @FXML
    private TableColumn<Customer, String> addressTableColumn;

    @FXML
    private TableColumn<Customer, String> districtTableColumn;

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
    private Label birthDateLabel;

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
        ButtonHelper.buttonCursor(filterButton, addButton, updateButton, deleteButton);
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

            StageHelper.createAddOrUpdateStage("Cadastrar cliente", root);

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
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um cliente para atualizar", "");
        } else {
            int index = customerTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/CustomersUpdate.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                CustomersUpdateController controller = loader.getController();
                controller.setLoadCustomer(selected);

                StageHelper.createAddOrUpdateStage("Atualizar cliente", root);

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
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um cliente para excluir", "");
        } else {
            alert.confirmationAlert("Excluir cliente", "Tem certeza que deseja excluir o(a) cliente '" + selected.getName() + "'? "
                    + "\n\nO(a) cliente será excluído(a) de forma definitiva e não poderá ser recuperado(a).");
            if (alert.getResult().get() == ButtonType.YES) {
                CustomerDao customerDao = new CustomerDao();
                CustomerDao customerDao1 = new CustomerDao();
                CustomerDao customerDao2 = new CustomerDao();
                CustomerDao customerDao3 = new CustomerDao();
                
                boolean saleConstraint = customerDao.checkSaleDeleteConstraint(selected.getCustomerId());
                boolean measurementConstraint = customerDao1.checkMeasurementDeleteConstraint(selected.getCustomerId());
                boolean sheetConstraint = customerDao2.checkSheetDeleteConstraint(selected.getCustomerId());
                boolean membershipConstraint = customerDao3.checkMembershipDeleteConstraint(selected.getCustomerId());

                if (saleConstraint || measurementConstraint || sheetConstraint || membershipConstraint) {
                    alert.customAlert(Alert.AlertType.WARNING, "Não foi possível excluir", "Existem vendas, medidas, fichas ou mensalidades relacionadas a este(a) cliente.");
                } else {
                    CustomerDao customerDao4 = new CustomerDao();
                    customerDao4.delete(selected);
                    refreshTableView();
                    resetDetails();
                    alert.customAlert(Alert.AlertType.WARNING, "Cliente excluído(a) com sucesso", "");
                }
            }
        }
    }

    private void resetDetails() {
        if (selected == null) {
            customerIdLabel.setText("");
            personRegistryLabel.setText("");
            nameLabel.setText("");
            sexLabel.setText("");
            birthDateLabel.setText("");
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
            birthDateLabel.setText(selected.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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
        personRegistryTableColumn.setCellValueFactory(new PropertyValueFactory("personRegistry"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        sexTableColumn.setCellValueFactory(new PropertyValueFactory("sex"));
        birthDateTableColumn.setCellValueFactory(new PropertyValueFactory("birthDate"));
        TextFieldFormat.customerTableCellDateFormat(birthDateTableColumn);
        emailTableColumn.setCellValueFactory(new PropertyValueFactory("email"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory("phone"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory("address"));
        districtTableColumn.setCellValueFactory(new PropertyValueFactory("district"));
        cityTableColumn.setCellValueFactory(new PropertyValueFactory("city"));
        federativeUnitTableColumn.setCellValueFactory(new PropertyValueFactory("federativeUnit"));
        refreshTableView();
    }

    private boolean caseSensitiveEnabled(Customer customer, String searchText, int optionOrder) {
        String personRegistry = customer.getPersonRegistry();
        String name = customer.getName();
        String sex = customer.getSex();
        String birthDate = customer.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String email = customer.getEmail();
        String phone = customer.getPhone();
        String address = customer.getAddress();
        String district = customer.getDistrict();
        String city = customer.getCity();
        String federativeUnit = customer.getFederativeUnit();

        List<String> fields = Arrays.asList(personRegistry, name, sex, birthDate, email, phone, address, district, city, federativeUnit);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(Customer customer, String searchText, int optionOrder) {
        String personRegistry = customer.getPersonRegistry();
        String name = customer.getName().toLowerCase();
        String sex = customer.getSex().toLowerCase();
        String birthDate = customer.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String email = customer.getEmail().toLowerCase();
        String phone = customer.getPhone();
        String address = customer.getAddress().toLowerCase();
        String district = customer.getDistrict().toLowerCase();
        String city = customer.getCity().toLowerCase();
        String federativeUnit = customer.getFederativeUnit().toLowerCase();

        List<String> fields = Arrays.asList(personRegistry, name, sex, birthDate, email, phone, address, district, city, federativeUnit);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        boolean searchReturn = false;
        switch (optionOrder) {
            case 1:
                searchReturn = (list.get(0).contains(searchText)) || (list.get(1).contains(searchText)) || (list.get(2).contains(searchText))
                        || (list.get(3).contains(searchText)) || (list.get(4).contains(searchText)) || (list.get(5).contains(searchText))
                        || (list.get(6).contains(searchText)) || (list.get(7).contains(searchText)) || (list.get(8).contains(searchText))
                        || (list.get(9).contains(searchText));
                break;
            case 2:
                searchReturn = (list.get(0).equals(searchText)) || (list.get(1).equals(searchText)) || (list.get(2).equals(searchText))
                        || (list.get(3).equals(searchText)) || (list.get(4).equals(searchText)) || (list.get(5).equals(searchText))
                        || (list.get(6).equals(searchText)) || (list.get(7).equals(searchText)) || (list.get(8).equals(searchText))
                        || (list.get(9).equals(searchText));
                break;
            case 3:
                searchReturn = (list.get(0).startsWith(searchText)) || (list.get(1).startsWith(searchText)) || (list.get(2).startsWith(searchText))
                        || (list.get(3).startsWith(searchText)) || (list.get(4).startsWith(searchText)) || (list.get(5).startsWith(searchText))
                        || (list.get(6).startsWith(searchText)) || (list.get(7).startsWith(searchText)) || (list.get(8).startsWith(searchText))
                        || (list.get(9).startsWith(searchText));
                break;
            case 4:
                searchReturn = (list.get(0).endsWith(searchText)) || (list.get(1).endsWith(searchText)) || (list.get(2).endsWith(searchText))
                        || (list.get(3).endsWith(searchText)) || (list.get(4).endsWith(searchText)) || (list.get(5).endsWith(searchText))
                        || (list.get(6).endsWith(searchText)) || (list.get(7).endsWith(searchText)) || (list.get(8).endsWith(searchText))
                        || (list.get(9).endsWith(searchText));
                break;
            default:
                break;
        }
        return searchReturn;
    }

    private void search() {
        FilteredList<Customer> filteredData = new FilteredList<>(loadData(), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(customer, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(customer, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(customer, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(customer, newValue, 4);
                }

                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(customer, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(customer, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(customer, newValue.toLowerCase(), 4);

                return caseSensitiveDisabled(customer, newValue.toLowerCase(), 1);
            });
            customerTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(customerTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<Customer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(customerTableView.comparatorProperty());
        customerTableView.setItems(sortedData);
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
    private void goToFirstRow() {
        ChangeTableRow.changeToFirstRow(customerTableView);
    }

    @FXML
    private void goToLastRow() {
        ChangeTableRow.changeToLastRow(customerTableView);
    }

}
