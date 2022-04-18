package boxgym.controller;

import boxgym.dao.StockEntryDao;
import boxgym.helper.ButtonHelper;
import boxgym.helper.StageHelper;
import boxgym.helper.TextFieldFormat;
import boxgym.model.StockEntry;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class StockController implements Initializable {
    
    private StockEntry selected;
    
    @FXML
    private MenuButton filterButton;
    
    @FXML
    private CheckMenuItem caseSensitiveOp;

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
    private MenuButton exportButton;
    
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ButtonHelper.buttonCursor(filterButton, exportButton, addButton);
        initSupplierTableView();
        tableViewListeners();
        Platform.runLater(() -> searchBox.requestFocus());
    }

    @FXML
    void add(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/StockEntryAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);
            
            StockEntryAddController controller = loader.getController();
            
            StageHelper.createAddOrUpdateStage("Adicionando Entrada de Estoque", root);
            
            if(controller.isStockEntryCreationFlag() && controller.isProductsEntryCreationFlag()) {
                refreshTableView();
                stockEntryTableView.getSelectionModel().selectLast();
            } else {
                StockEntryDao stockEntryDao = new StockEntryDao();
                stockEntryDao.deleteLastEntry();
            }
        } catch (IOException ex) {
            Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private void showDetails() {
        
    }
    
    private ObservableList<StockEntry> loadData() {
        StockEntryDao stockEntryDao = new StockEntryDao();
        return FXCollections.observableArrayList(stockEntryDao.read());
    }

    private void refreshTableView() {
        stockEntryTableView.setItems(loadData());
        search();
    }

    private void initSupplierTableView() {
        stockIdTableColumn.setCellValueFactory(new PropertyValueFactory("stockEntryId"));
        fkSupplierTableColumn.setCellValueFactory(new PropertyValueFactory("tempSupplierName"));
        
        invoiceIssueDateTableColumn.setCellValueFactory(new PropertyValueFactory("invoiceIssueDate"));
        TextFieldFormat.stockEntryTableCellDateFormat(invoiceIssueDateTableColumn);
        
        invoiceNumberTableColumn.setCellValueFactory(new PropertyValueFactory("invoiceNumber"));
        refreshTableView();
    }
    
    private void search() {
        
    }
    
    private void tableViewListeners() {
        stockEntryTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selected = (StockEntry) newValue;
                showDetails();
            }
        });

        /*supplierTableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            final TableHeaderRow header = (TableHeaderRow) supplierTableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
        });*/
    }
    
    @FXML
    void exportToExcel(ActionEvent event) {

    }

    @FXML
    void generatePdf(ActionEvent event) {

    }
}
