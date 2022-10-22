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
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TableView<Billing> billingTableView;
    
    @FXML
    private TableColumn<Billing, String> fkCustomerTableColumn;
    
    @FXML
    private TableColumn<Billing, String> descriptionTableColumn;
    
    @FXML
    private TableColumn<Billing, LocalDate> expirationDateTableColumn;
    
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
    private Label descriptionLabel;
    
    @FXML
    private Label expirationDateLabel;
    
    @FXML
    private Label valueToPayLabel;
    
    @FXML
    private Label createdAtLabel;
    
    @FXML
    private Label updatedAtLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initBillingTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }
    
    private void resetDetails() {
        if (selected == null) {
            billingIdLabel.setText("");
            descriptionLabel.setText("");
            expirationDateLabel.setText("");
            valueToPayLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }
    
    private void showDetails() {
        if (selected != null) {
            billingIdLabel.setText(String.valueOf(selected.getBillingId()));
            descriptionLabel.setText(selected.getDescription());
            expirationDateLabel.setText(selected.getExpirationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            TextFieldFormat.currencyFormat(valueToPayLabel, selected.getValueToPay());
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }
    
    private ObservableList<Billing> loadData() {
        BillingDao billingDao = new BillingDao();
        return FXCollections.observableArrayList(billingDao.readSales());
    }

    private void refreshTableView() {
        billingTableView.setItems(loadData());
        //search();
        countLabel.setText(TableViewCount.footerMessage(billingTableView.getItems().size(), "resultado"));
    }

    private void initBillingTableView() {
        fkCustomerTableColumn.setCellValueFactory(new PropertyValueFactory("tempCustomerName"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory("description"));
        expirationDateTableColumn.setCellValueFactory(new PropertyValueFactory("expirationDate"));
        TextFieldFormat.billingTableCellDateFormat(expirationDateTableColumn);
        valueToPayTableColumn.setCellValueFactory(new PropertyValueFactory("valueToPay"));
        TextFieldFormat.billingTableCellCurrencyFormat(valueToPayTableColumn);
        refreshTableView();
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
