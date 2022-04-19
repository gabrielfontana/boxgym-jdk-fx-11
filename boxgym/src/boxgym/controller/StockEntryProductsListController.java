package boxgym.controller;

import boxgym.dao.StockEntryProductDao;
import boxgym.model.StockEntryProduct;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StockEntryProductsListController implements Initializable {
     
    private int selectedStockEntry;
    
    @FXML
    private TableView<StockEntryProduct> productsListTableView;
    
    @FXML
    private TableColumn<StockEntryProduct, String> productTableColumn;
    
    @FXML
    private TableColumn<StockEntryProduct, Integer> amountTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, BigDecimal> costPriceTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, BigDecimal> totalTableColumn;
    
    @FXML
    private Label countLabel;

    public int getSelectedStockEntry() {
        return selectedStockEntry;
    }

    public void setSelectedStockEntry(int selectedStockEntry) {
        this.selectedStockEntry = selectedStockEntry;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> initMethods());
    }
    
    private void initMethods(){
        initProductsListTableView();
        initCount();
    }
    
    private ObservableList<StockEntryProduct> loadData() {
        StockEntryProductDao dao = new StockEntryProductDao();
        return FXCollections.observableArrayList(dao.read(getSelectedStockEntry()));
    }
    
    private void initProductsListTableView() {
        productTableColumn.setCellValueFactory(new PropertyValueFactory("tempProductName"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory("amount"));
        costPriceTableColumn.setCellValueFactory(new PropertyValueFactory("costPrice"));
        totalTableColumn.setCellValueFactory(new PropertyValueFactory("total"));
        productsListTableView.setItems(loadData());
    }
    
    private void initCount() {
        StockEntryProductDao dao = new StockEntryProductDao();
        countLabel.setText(String.valueOf(dao.count(getSelectedStockEntry())));
    }
}
