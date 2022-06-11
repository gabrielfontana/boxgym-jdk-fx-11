package boxgym.controller;

import boxgym.dao.StockEntryDao;
import boxgym.dao.StockEntryProductDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.StockEntry;
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
import javafx.scene.input.MouseEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class StockEntryController implements Initializable {

    AlertHelper alert = new AlertHelper();

    private StockEntry selected;

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
    private Button deleteButton;

    @FXML
    private TableView<StockEntry> stockEntryTableView;

    @FXML
    private TableColumn<StockEntry, Integer> stockIdTableColumn;

    @FXML
    private TableColumn<StockEntry, String> fkSupplierTableColumn;

    @FXML
    private TableColumn<StockEntry, LocalDate> invoiceIssueDateTableColumn;

    @FXML
    private TableColumn<StockEntry, String> invoiceNumberTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private Label selectedRowLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private Label stockEntryIdLabel;

    @FXML
    private Label fkSupplierLabel;

    @FXML
    private Label invoiceIssueDateLabel;

    @FXML
    private Label invoiceNumberLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label updatedAtLabel;

    @FXML
    private Button listButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton, addButton, deleteButton, listButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initStockEntryTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }

    @FXML
    void addStockEntry(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/StockEntryAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            StockEntryAddController controller = loader.getController();

            StageHelper.createAddOrUpdateStage("Adicionando Entrada de Estoque", root);

            if (controller.isStockEntryCreationFlag() && !controller.isProductsEntryCreationFlag()) {
                StockEntryDao stockEntryDao = new StockEntryDao();
                stockEntryDao.deleteLastEntry();
            } else if (controller.isStockEntryCreationFlag() && controller.isProductsEntryCreationFlag()) {
                refreshTableView();
                stockEntryTableView.getSelectionModel().selectLast();
            }
        } catch (IOException ex) {
            Logger.getLogger(StockEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void deleteStockEntry(ActionEvent event) {
        StockEntryDao stockEntryDao = new StockEntryDao();
        StockEntryProductDao stockEntryProductDao = new StockEntryProductDao();

        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione uma entrada de estoque para excluir!", "");
        } else {
            alert.confirmationAlert("Tem certeza que deseja excluir esta entrada de estoque?", "Esta ação é irreversível!");
            if (alert.getResult().get() == ButtonType.YES) {
                stockEntryProductDao.delete(selected);
                stockEntryDao.delete(selected);
                refreshTableView();
                resetDetails();
                alert.customAlert(Alert.AlertType.WARNING, "A entrada de estoque foi excluída com sucesso!", "");
            }
        }
    }

    private void resetDetails() {
        if (selected == null) {
            stockEntryIdLabel.setText("");
            fkSupplierLabel.setText("");
            invoiceIssueDateLabel.setText("");
            invoiceNumberLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            stockEntryIdLabel.setText(String.valueOf(selected.getStockEntryId()));
            fkSupplierLabel.setText(selected.getTempSupplierName());
            invoiceIssueDateLabel.setText(selected.getInvoiceIssueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            invoiceNumberLabel.setText(selected.getInvoiceNumber());
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }

    @FXML
    void listProducts(ActionEvent event) {
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione uma entrada de estoque para listar os produtos!", "");
        } else {
            int index = stockEntryTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/StockEntryProductsList.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                StockEntryProductsListController controller = loader.getController();
                controller.setSelectedStockEntry(selected.getStockEntryId());

                StageHelper.createAddOrUpdateStage("Listando Produtos da Entrada de Estoque", root);
                stockEntryTableView.getSelectionModel().select(index);
            } catch (IOException ex) {
                Logger.getLogger(StockEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private ObservableList<StockEntry> loadData() {
        StockEntryDao stockEntryDao = new StockEntryDao();
        return FXCollections.observableArrayList(stockEntryDao.read());
    }

    private void refreshTableView() {
        stockEntryTableView.setItems(loadData());
        search();
        countLabel.setText(TableViewCount.footerMessage(stockEntryTableView.getItems().size(), "resultado"));
    }

    private void initStockEntryTableView() {
        stockIdTableColumn.setCellValueFactory(new PropertyValueFactory("stockEntryId"));
        fkSupplierTableColumn.setCellValueFactory(new PropertyValueFactory("tempSupplierName"));

        invoiceIssueDateTableColumn.setCellValueFactory(new PropertyValueFactory("invoiceIssueDate"));
        TextFieldFormat.stockEntryTableCellDateFormat(invoiceIssueDateTableColumn);

        invoiceNumberTableColumn.setCellValueFactory(new PropertyValueFactory("invoiceNumber"));
        refreshTableView();
    }

    private boolean caseSensitiveEnabled(StockEntry stockEntry, String searchText, int optionOrder) {
        String stockEntryId = String.valueOf(stockEntry.getStockEntryId());
        String tempSupplierName = stockEntry.getTempSupplierName();
        String invoiceIssueDate = stockEntry.getInvoiceIssueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String invoiceNumber = stockEntry.getInvoiceNumber();

        List<String> fields = Arrays.asList(stockEntryId, tempSupplierName, invoiceIssueDate, invoiceNumber);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(StockEntry stockEntry, String searchText, int optionOrder) {
        String stockEntryId = String.valueOf(stockEntry.getStockEntryId());
        String tempSupplierName = stockEntry.getTempSupplierName().toLowerCase();
        String invoiceIssueDate = stockEntry.getInvoiceIssueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String invoiceNumber = stockEntry.getInvoiceNumber();

        List<String> fields = Arrays.asList(stockEntryId, tempSupplierName, invoiceIssueDate, invoiceNumber);

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

    private void search() {
        FilteredList<StockEntry> filteredData = new FilteredList<>(loadData(), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(stockEntry -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(stockEntry, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(stockEntry, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(stockEntry, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(stockEntry, newValue, 4);
                } 
                
                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(stockEntry, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(stockEntry, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(stockEntry, newValue.toLowerCase(), 4);
                
                return caseSensitiveDisabled(stockEntry, newValue.toLowerCase(), 1);
            });
            stockEntryTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(stockEntryTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<StockEntry> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(stockEntryTableView.comparatorProperty());
        stockEntryTableView.setItems(sortedData);
    }

    private void listeners() {
        stockEntryTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (StockEntry) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(stockEntryTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
        stockEntryTableView.scrollTo(0);
        stockEntryTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (stockEntryTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            stockEntryTableView.scrollTo(stockEntryTableView.getItems().size() - 1);
            stockEntryTableView.getSelectionModel().selectLast();
        }
    }
}
