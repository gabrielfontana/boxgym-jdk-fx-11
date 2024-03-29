package boxgym.controller;

import boxgym.dao.SupplierDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.ChangeTableRow;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.model.Supplier;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
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

public class SuppliersController implements Initializable {

    AlertHelper alert = new AlertHelper();

    private Supplier selected;

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
    private TableView<Supplier> supplierTableView;

    @FXML
    private TableColumn<Supplier, String> companyRegistryTableColumn;

    @FXML
    private TableColumn<Supplier, String> corporateNameTableColumn;

    @FXML
    private TableColumn<Supplier, String> tradeNameTableColumn;
    
    @FXML
    private TableColumn<Supplier, String> emailTableColumn;

    @FXML
    private TableColumn<Supplier, String> phoneTableColumn;

    @FXML
    private TableColumn<Supplier, String> addressTableColumn;
    
    @FXML
    private TableColumn<Supplier, String> districtTableColumn;

    @FXML
    private TableColumn<Supplier, String> cityTableColumn;

    @FXML
    private TableColumn<Supplier, String> federativeUnitTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Label supplierIdLabel;

    @FXML
    private Label companyRegistryLabel;

    @FXML
    private Label corporateNameLabel;

    @FXML
    private Label tradeNameLabel;

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
        initSupplierTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }

    @FXML
    void addSupplier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/SuppliersAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            SuppliersAddController controller = loader.getController();

            StageHelper.createAddOrUpdateStage("Cadastrar fornecedor", root);

            if (controller.isCreated()) {
                refreshTableView();
                supplierTableView.getSelectionModel().selectLast();
            }
        } catch (IOException ex) {
            Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void updateSupplier(ActionEvent event) {
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um fornecedor para atualizar", "");
        } else {
            int index = supplierTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/SuppliersUpdate.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                SuppliersUpdateController controller = loader.getController();
                controller.setLoadSupplier(selected);

                StageHelper.createAddOrUpdateStage("Atualizar fornecedor", root);

                if (controller.isUpdated()) {
                    refreshTableView();
                    supplierTableView.getSelectionModel().select(index);
                }
            } catch (IOException ex) {
                Logger.getLogger(SuppliersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void deleteSupplier(ActionEvent event) {
        SupplierDao supplierDao = new SupplierDao();

        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um fornecedor para excluir", "");
        } else {
            alert.confirmationAlert("Excluir fornecedor", "Tem certeza que deseja excluir o fornecedor '" + selected.getTradeName() + "'? "
                    + "\n\nO fornecedor será excluído de forma definitiva e não poderá ser recuperado.");
            if (alert.getResult().get() == ButtonType.YES) {
                if (supplierDao.checkStockEntryDeleteConstraint(selected.getSupplierId())) {
                    alert.customAlert(Alert.AlertType.WARNING, "Não foi possível excluir", "Existem entradas de estoque relacionadas a este(a) fornecedor.");
                } else {
                    supplierDao.delete(selected);
                    refreshTableView();
                    resetDetails();
                    alert.customAlert(Alert.AlertType.WARNING, "Fornecedor excluído com sucesso", "");
                }
            }
        }
    }

    private void resetDetails() {
        if (selected == null) {
            supplierIdLabel.setText("");
            companyRegistryLabel.setText("");
            corporateNameLabel.setText("");
            tradeNameLabel.setText("");
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
            supplierIdLabel.setText(String.valueOf(selected.getSupplierId()));
            companyRegistryLabel.setText(selected.getCompanyRegistry());
            corporateNameLabel.setText(selected.getCorporateName());
            tradeNameLabel.setText(selected.getTradeName());
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

    private ObservableList<Supplier> loadData() {
        SupplierDao supplierDao = new SupplierDao();
        return FXCollections.observableArrayList(supplierDao.read());
    }

    private void refreshTableView() {
        supplierTableView.setItems(loadData());
        search();
        countLabel.setText(TableViewCount.footerMessage(supplierTableView.getItems().size(), "resultado"));
    }

    private void initSupplierTableView() {
        companyRegistryTableColumn.setCellValueFactory(new PropertyValueFactory("companyRegistry"));
        corporateNameTableColumn.setCellValueFactory(new PropertyValueFactory("corporateName"));
        tradeNameTableColumn.setCellValueFactory(new PropertyValueFactory("tradeName"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory("email"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory("phone"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory("address"));
        districtTableColumn.setCellValueFactory(new PropertyValueFactory("district"));
        cityTableColumn.setCellValueFactory(new PropertyValueFactory("city"));
        federativeUnitTableColumn.setCellValueFactory(new PropertyValueFactory("federativeUnit"));
        refreshTableView();
    }

    private boolean caseSensitiveEnabled(Supplier supplier, String searchText, int optionOrder) {
        String companyRegistry = supplier.getCompanyRegistry();
        String corporateName = supplier.getCorporateName();
        String tradeName = supplier.getTradeName();
        String email = supplier.getEmail();
        String phone = supplier.getPhone();
        String address = supplier.getAddress();
        String district = supplier.getDistrict();
        String city = supplier.getCity();
        String federativeUnit = supplier.getFederativeUnit();

        List<String> fields = Arrays.asList(companyRegistry, corporateName, tradeName, email, phone, address, district, city, federativeUnit);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(Supplier supplier, String searchText, int optionOrder) {
        String companyRegistry = supplier.getCompanyRegistry();
        String corporateName = supplier.getCorporateName().toLowerCase();
        String tradeName = supplier.getTradeName().toLowerCase();
        String email = supplier.getEmail().toLowerCase();
        String phone = supplier.getPhone();
        String address = supplier.getAddress().toLowerCase();
        String district = supplier.getDistrict().toLowerCase();
        String city = supplier.getCity().toLowerCase();
        String federativeUnit = supplier.getFederativeUnit().toLowerCase();

        List<String> fields = Arrays.asList(companyRegistry, corporateName, tradeName, email, phone, address, district, city, federativeUnit);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        boolean searchReturn = false;
        switch (optionOrder) {
            case 1:
                searchReturn = (list.get(0).contains(searchText)) || (list.get(1).contains(searchText)) || (list.get(2).contains(searchText))
                        || (list.get(3).contains(searchText)) || (list.get(4).contains(searchText)) || (list.get(5).contains(searchText))
                        || (list.get(6).contains(searchText)) || (list.get(7).contains(searchText)) || (list.get(8).contains(searchText));
                break;
            case 2:
                searchReturn = (list.get(0).equals(searchText)) || (list.get(1).equals(searchText)) || (list.get(2).equals(searchText))
                        || (list.get(3).equals(searchText)) || (list.get(4).equals(searchText)) || (list.get(5).equals(searchText))
                        || (list.get(6).equals(searchText)) || (list.get(7).equals(searchText)) || (list.get(8).equals(searchText));
                break;
            case 3:
                searchReturn = (list.get(0).startsWith(searchText)) || (list.get(1).startsWith(searchText)) || (list.get(2).startsWith(searchText))
                        || (list.get(3).startsWith(searchText)) || (list.get(4).startsWith(searchText)) || (list.get(5).startsWith(searchText))
                        || (list.get(6).startsWith(searchText)) || (list.get(7).startsWith(searchText)) || (list.get(8).startsWith(searchText));
                break;
            case 4:
                searchReturn = (list.get(0).endsWith(searchText)) || (list.get(1).endsWith(searchText)) || (list.get(2).endsWith(searchText))
                        || (list.get(3).endsWith(searchText)) || (list.get(4).endsWith(searchText)) || (list.get(5).endsWith(searchText))
                        || (list.get(6).endsWith(searchText)) || (list.get(7).endsWith(searchText)) || (list.get(8).endsWith(searchText));
                break;
            default:
                break;
        }
        return searchReturn;
    }

    private void search() {
        FilteredList<Supplier> filteredData = new FilteredList<>(loadData(), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(supplier, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(supplier, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(supplier, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(supplier, newValue, 4);
                }

                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(supplier, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(supplier, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(supplier, newValue.toLowerCase(), 4);

                return caseSensitiveDisabled(supplier, newValue.toLowerCase(), 1);
            });
            supplierTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(supplierTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<Supplier> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(supplierTableView.comparatorProperty());
        supplierTableView.setItems(sortedData);
    }

    private void listeners() {
        supplierTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Supplier) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(supplierTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
        ChangeTableRow.changeToFirstRow(supplierTableView);
    }

    @FXML
    private void goToLastRow() {
        ChangeTableRow.changeToLastRow(supplierTableView);
    }

}
