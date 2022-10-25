package boxgym.controller;

import boxgym.dao.PaymentDao;
import boxgym.dao.SaleDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Payment;
import boxgym.model.Sale;
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
import org.controlsfx.control.PrefixSelectionComboBox;

public class PaymentsController implements Initializable {
    
    AlertHelper alert = new AlertHelper();

    private Payment selected;
    
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
    private PrefixSelectionComboBox<?> changeTableDataComboBox;
    
    @FXML
    private TableView<Payment> paymentTableView;
    
    @FXML
    private TableColumn<Payment, String> tempCustomerNameTableColumn;
    
    @FXML
    private TableColumn<Payment, String> descriptionTableColumn;
    
    @FXML
    private TableColumn<Payment, LocalDate> paymentDateTableColumn;
    
    @FXML
    private TableColumn<Payment, BigDecimal> tempValueToPayTableColumn;
    
    @FXML
    private TableColumn<Payment, BigDecimal> paidValueTableColumn;
    
    @FXML
    private Label countLabel;
    
    @FXML
    private Label selectedRowLabel;
    
    @FXML
    private MaterialDesignIconView firstRow;
    
    @FXML
    private MaterialDesignIconView lastRow;
    
    @FXML
    private Label paymentIdLabel;
    
    @FXML
    private Label tempCustomerNameLabel;
    
    @FXML
    private Label descriptionLabel;
    
    @FXML
    private Label paymentDateLabel;
    
    @FXML
    private Label tempValueToPayLabel;
    
    @FXML
    private Label paidValueLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initPaymentTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }    

    private void resetDetails() {
        if (selected == null) {
            paymentIdLabel.setText("");
            tempCustomerNameLabel.setText("");
            descriptionLabel.setText("");
            paymentDateLabel.setText("");
            tempValueToPayLabel.setText("");
            paidValueLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            paymentIdLabel.setText(String.valueOf(selected.getPaymentId()));
            tempCustomerNameLabel.setText(selected.getTempCustomerName());
            descriptionLabel.setText(selected.getDescription());
            paymentDateLabel.setText(selected.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            TextFieldFormat.currencyFormat(tempValueToPayLabel, selected.getTempValueToPay());
            TextFieldFormat.currencyFormat(paidValueLabel, selected.getPaidValue());
        }
    }
    
    private ObservableList<Payment> loadData() {
        PaymentDao paymentDao = new PaymentDao();
        return FXCollections.observableArrayList(paymentDao.read());
    }

    private void refreshTableView() {
        paymentTableView.setItems(loadData());
        //search();
        countLabel.setText(TableViewCount.footerMessage(paymentTableView.getItems().size(), "resultado"));
    }

    private void initPaymentTableView() {
        tempCustomerNameTableColumn.setCellValueFactory(new PropertyValueFactory("tempCustomerName"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory("description"));
        paymentDateTableColumn.setCellValueFactory(new PropertyValueFactory("paymentDate"));
        TextFieldFormat.paymentTableCellDateFormat(paymentDateTableColumn);
        tempValueToPayTableColumn.setCellValueFactory(new PropertyValueFactory("tempValueToPay"));
        TextFieldFormat.paymentTableCellCurrencyFormat(tempValueToPayTableColumn);
        paidValueTableColumn.setCellValueFactory(new PropertyValueFactory("paidValue"));
        TextFieldFormat.paymentTableCellCurrencyFormat(paidValueTableColumn);
        
        refreshTableView();
    }
    
    private void listeners() {
        paymentTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Payment) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(paymentTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
        paymentTableView.scrollTo(0);
        paymentTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (paymentTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            paymentTableView.getSelectionModel().selectLast();
            paymentTableView.scrollTo(paymentTableView.getItems().size() - 1);
        }
    }
    
}
