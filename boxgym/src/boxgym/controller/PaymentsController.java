package boxgym.controller;

import boxgym.dao.PaymentDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.ChangeTableRow;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Payment;
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
        search();
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
    
    private boolean caseSensitiveEnabled(Payment payment, String searchText, int optionOrder) {
        String tempCustomerName = payment.getTempCustomerName();
        String description = payment.getDescription();
        String paymentDate = payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String tempValueToPay = "R$ ".concat(String.valueOf(payment.getTempValueToPay()).replace(".", ","));
        String paidValue = "R$ ".concat(String.valueOf(payment.getPaidValue()).replace(".", ","));
        
        List<String> fields = Arrays.asList(tempCustomerName, description, paymentDate, tempValueToPay, paidValue);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(Payment payment, String searchText, int optionOrder) {
        String tempCustomerName = payment.getTempCustomerName().toLowerCase();
        String description = payment.getDescription().toLowerCase();
        String paymentDate = payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String tempValueToPay = "r$ ".concat(String.valueOf(payment.getTempValueToPay()).replace(".", ","));
        String paidValue = "r$ ".concat(String.valueOf(payment.getPaidValue()).replace(".", ","));

        List<String> fields = Arrays.asList(tempCustomerName, description, paymentDate, tempValueToPay, paidValue);

        return stringComparasion(fields, searchText, optionOrder);
    }
    
    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        boolean searchReturn = false;
        switch (optionOrder) {
            case 1:
                searchReturn = (list.get(0).contains(searchText)) || (list.get(1).contains(searchText))
                        || (list.get(2).contains(searchText)) || (list.get(3).contains(searchText)) || (list.get(4).contains(searchText));
                break;
            case 2:
                searchReturn = (list.get(0).equals(searchText)) || (list.get(1).equals(searchText))
                        || (list.get(2).equals(searchText)) || (list.get(3).equals(searchText)) || (list.get(4).equals(searchText));
                break;
            case 3:
                searchReturn = (list.get(0).startsWith(searchText)) || (list.get(1).startsWith(searchText))
                        || (list.get(2).startsWith(searchText)) || (list.get(3).startsWith(searchText)) || (list.get(4).startsWith(searchText));
                break;
            case 4:
                searchReturn = (list.get(0).endsWith(searchText)) || (list.get(1).endsWith(searchText))
                        || (list.get(2).endsWith(searchText)) || (list.get(3).endsWith(searchText)) || (list.get(4).endsWith(searchText));
                break;
            default:
                break;
        }
        return searchReturn;
    }
    
    private void search() {
        FilteredList<Payment> filteredData = new FilteredList<>(loadData(), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(payment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(payment, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(payment, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(payment, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(payment, newValue, 4);
                }

                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(payment, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(payment, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(payment, newValue.toLowerCase(), 4);

                return caseSensitiveDisabled(payment, newValue.toLowerCase(), 1);
            });
            paymentTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(paymentTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<Payment> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(paymentTableView.comparatorProperty());
        paymentTableView.setItems(sortedData);
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
    private void goToFirstRow() {
        ChangeTableRow.changeToFirstRow(paymentTableView);
    }

    @FXML
    private void goToLastRow() {
        ChangeTableRow.changeToLastRow(paymentTableView);
    }
    
}
