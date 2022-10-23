package boxgym.controller;

import boxgym.dao.BillingDao;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Billing;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import org.controlsfx.control.PrefixSelectionComboBox;

public class BillingsController implements Initializable {

    private Billing selected;

    @FXML
    private MenuButton filterButton;

    @FXML
    private CheckMenuItem caseSensitiveOp;

    @FXML
    private RadioMenuItem containsOp;

    @FXML
    private ToggleGroup filterOptions;

    @FXML
    private RadioMenuItem alphabeticalEqualsToOp;

    @FXML
    private RadioMenuItem startsWithOp;

    @FXML
    private RadioMenuItem endsWithOp;

    @FXML
    private TextField searchBox;

    @FXML
    private PrefixSelectionComboBox<String> changeTableDataComboBox;

    @FXML
    private TableView<Billing> billingTableView;

    @FXML
    private TableColumn<Billing, String> tempCustomerNameTableColumn;

    @FXML
    private TableColumn<Billing, String> descriptionTableColumn;

    @FXML
    private TableColumn<Billing, LocalDate> dueDateTableColumn;

    @FXML
    private TableColumn<Billing, BigDecimal> valueToPayTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Label billingIdLabel;
    
    @FXML
    private Label tempCustomerNameLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Label valueToPayLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label updatedAtLabel;

    List<String> billingTypeList = Arrays.asList("Todas", "Vendas", "Mensalidades");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        loadChangeTableDataComboBox();
        changeTableDataComboBoxListener();
        initBillingTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }

    private void resetDetails() {
        if (selected == null) {
            billingIdLabel.setText("");
            tempCustomerNameLabel.setText("");
            descriptionLabel.setText("");
            dueDateLabel.setText("");
            valueToPayLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            billingIdLabel.setText(String.valueOf(selected.getBillingId()));
            tempCustomerNameLabel.setText(selected.getTempCustomerName());
            descriptionLabel.setText(selected.getDescription());
            dueDateLabel.setText(selected.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            TextFieldFormat.currencyFormat(valueToPayLabel, selected.getValueToPay());
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }

    private void loadChangeTableDataComboBox() {
        changeTableDataComboBox.setItems(FXCollections.observableArrayList(billingTypeList));
        changeTableDataComboBox.getSelectionModel().selectFirst();
    }

    private void changeTableDataComboBoxListener() {
        changeTableDataComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (changeTableDataComboBox.getSelectionModel().getSelectedItem().equals(billingTypeList.get(0))) {
                refreshTableView(billingTypeList.get(0)); //Todas
            } else if (changeTableDataComboBox.getSelectionModel().getSelectedItem().equals(billingTypeList.get(1))) {
                refreshTableView(billingTypeList.get(1)); //Vendas
            } else {
                refreshTableView(billingTypeList.get(2)); //Mensalidades
            }
        });
    }

    private void refreshTableView(String selectedItem) {
        for (int i = 0; i < billingTypeList.size(); i++) {
            if (selectedItem.equals(billingTypeList.get(i))) {
                billingTableView.setItems(loadData(billingTypeList.get(i)));
                search(billingTypeList.get(i));
            }
        }
        countLabel.setText(TableViewCount.footerMessage(billingTableView.getItems().size(), "resultado"));
    }

    private ObservableList<Billing> loadData(String selectedItem) {
        BillingDao billingDao = new BillingDao();
        ObservableList<Billing> list;
        
        if (selectedItem.equals(billingTypeList.get(0))) {
            list = FXCollections.observableArrayList(billingDao.readAll()); //Todas
        } else if (selectedItem.equals(billingTypeList.get(1))) {
            list = FXCollections.observableArrayList(billingDao.readSales()); //Vendas
        } else {
            list = FXCollections.observableArrayList(billingDao.readMembership()); //Mensalidades
        }

        return list;
    }

    private void initBillingTableView() {
        tempCustomerNameTableColumn.setCellValueFactory(new PropertyValueFactory("tempCustomerName"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory("description"));
        dueDateTableColumn.setCellValueFactory(new PropertyValueFactory("dueDate"));
        TextFieldFormat.billingTableCellDateFormat(dueDateTableColumn);
        valueToPayTableColumn.setCellValueFactory(new PropertyValueFactory("valueToPay"));
        TextFieldFormat.billingTableCellCurrencyFormat(valueToPayTableColumn);
        refreshTableView(billingTypeList.get(0));
    }

    private boolean caseSensitiveEnabled(Billing billing, String searchText, int optionOrder) {
        String tempCustomerName = billing.getTempCustomerName();
        String description = billing.getDescription();
        String dueDate = billing.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String valueToPay = "R$ ".concat(String.valueOf(billing.getValueToPay()).replace(".", ","));

        List<String> fields = Arrays.asList(tempCustomerName, description, dueDate, valueToPay);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(Billing billing, String searchText, int optionOrder) {
        String tempCustomerName = billing.getTempCustomerName();
        String description = billing.getDescription().toLowerCase();
        String dueDate = billing.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String valueToPay = "r$ ".concat(String.valueOf(billing.getValueToPay()).replace(".", ","));

        List<String> fields = Arrays.asList(tempCustomerName, description, dueDate, valueToPay);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        boolean searchReturn = false;
        switch (optionOrder) {
            case 1:
                searchReturn = (list.get(0).contains(searchText)) || (list.get(1).contains(searchText))
                        || (list.get(2).contains(searchText)) || (list.get(3).contains(searchText));
                break;
            case 2:
                searchReturn = (list.get(0).equals(searchText)) || (list.get(1).equals(searchText))
                        || (list.get(2).equals(searchText)) || (list.get(3).equals(searchText));
                break;
            case 3:
                searchReturn = (list.get(0).startsWith(searchText)) || (list.get(1).startsWith(searchText))
                        || (list.get(2).startsWith(searchText)) || (list.get(3).startsWith(searchText));
                break;
            case 4:
                searchReturn = (list.get(0).endsWith(searchText)) || (list.get(1).endsWith(searchText))
                        || (list.get(2).endsWith(searchText)) || (list.get(3).endsWith(searchText));
                break;
            default:
                break;
        }
        return searchReturn;
    }

    private void search(String selectedItem) {
        FilteredList<Billing> filteredData = new FilteredList<>(loadData(selectedItem), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(billing -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(billing, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(billing, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(billing, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(billing, newValue, 4);
                }

                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(billing, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(billing, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(billing, newValue.toLowerCase(), 4);

                return caseSensitiveDisabled(billing, newValue.toLowerCase(), 1);
            });
            billingTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(billingTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<Billing> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(billingTableView.comparatorProperty());
        billingTableView.setItems(sortedData);
    }
    
    private void listeners() {
        billingTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Billing) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(billingTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
    void goToFirstRow(MouseEvent event) {
        billingTableView.scrollTo(0);
        billingTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (billingTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            billingTableView.getSelectionModel().selectLast();
            billingTableView.scrollTo(billingTableView.getItems().size() - 1);
        }
    }

}
