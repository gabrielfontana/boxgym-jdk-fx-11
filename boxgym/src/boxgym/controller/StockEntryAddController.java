package boxgym.controller;

import boxgym.dao.ProductDao;
import boxgym.dao.StockEntryDao;
import boxgym.dao.SupplierDao;
import boxgym.helper.ActionButtonTableCell;
import boxgym.helper.ButtonHelper;
import boxgym.model.StockEntry;
import boxgym.model.StockEntryProduct;
import currencyfield.CurrencyField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;

public class StockEntryAddController implements Initializable {

    SupplierDao supplierDao = new SupplierDao();
    LinkedHashMap<Integer, String> supplierMap = supplierDao.getSupplierForHashMap();

    ProductDao produtDao = new ProductDao();
    LinkedHashMap<Integer, String> productMap = produtDao.getProductForHashMap();
    
    List<StockEntryProduct> list = new ArrayList<>();
    ObservableList<StockEntryProduct> obsListItens;
    
    @FXML
    private AnchorPane stockEntryArea;

    @FXML
    private ComboBox<String> supplierComboBox;

    @FXML
    private DatePicker invoiceIssueDateDatePicker;

    @FXML
    private LimitedTextField invoiceNumberTextField;

    @FXML
    private Button addStockEntryButton;

    @FXML
    private AnchorPane productsEntryArea;

    @FXML
    private TextField stockEntryIdTextField;

    @FXML
    private ComboBox<String> productComboBox;

    @FXML
    private LimitedTextField amountTextField;

    @FXML
    private CurrencyField costPriceTextField;

    @FXML
    private Button addProductEntryButton;

    @FXML
    private TableView<StockEntryProduct> productEntryTableView;

    @FXML
    private TableColumn<StockEntryProduct, Integer> productTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, Integer> amountTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, BigDecimal> costPriceTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, BigDecimal> totalTableColumn;
    
     @FXML
    private TableColumn<StockEntryProduct, Button> actionButtonTableColumn;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;
    
    private boolean created = false;
    
    private StockEntryProduct selected;

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCreated(false);
        loadSupplierNameComboBox();
        loadProductNameComboBox();
        productNameComboBoxListener();
        ButtonHelper.buttonCursor(addStockEntryButton, addProductEntryButton, saveButton, clearButton);
        tableViewListeners();
        initProductEntryTableView();
    }

    private void loadSupplierNameComboBox() {
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (String s : supplierMap.values()) {
            obsList.add(s);
        }
        supplierComboBox.setPromptText("Selecione");
        supplierComboBox.setItems(obsList);
    }

    private int getKeyFromSupplierComboBox() {
        int key = 0;
        for (Map.Entry<Integer, String> entry : supplierMap.entrySet()) {
            if (supplierComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    private void loadProductNameComboBox() {
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (String s : productMap.values()) {
            obsList.add(s);
        }
        productComboBox.setPromptText("Selecione");
        productComboBox.setItems(obsList);
    }

    private int getKeyFromProductComboBox() {
        int key = 0;
        for (Map.Entry<Integer, String> entry : productMap.entrySet()) {
            if (productComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    private void productNameComboBoxListener() {
        productComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (productComboBox.getSelectionModel().getSelectedItem() != null) {
                ProductDao dao = new ProductDao();
                costPriceTextField.setPrice(dao.getProductCostPrice(getKeyFromProductComboBox()).doubleValue());
            }
        });
    }

    @FXML
    void addStockEntry(ActionEvent event) {
        stockEntryArea.setDisable(true);
        productsEntryArea.setDisable(false);
        StockEntry stockEntry = new StockEntry(getKeyFromSupplierComboBox(), invoiceIssueDateDatePicker.getValue(), invoiceNumberTextField.getText());
        StockEntryDao stockEntryDao = new StockEntryDao();
        stockEntryDao.create(stockEntry);
        stockEntryIdTextField.setText(String.valueOf(stockEntryDao.getStockEntryId()));
    }

    @FXML
    void addProductEntry(ActionEvent event) {
        int amount = Integer.valueOf(amountTextField.getText());
        BigDecimal costPrice = new BigDecimal(costPriceTextField.getPrice());
        BigDecimal total = costPrice.multiply(BigDecimal.valueOf(amount));
        StockEntryProduct item = new StockEntryProduct(Integer.valueOf(stockEntryIdTextField.getText()), getKeyFromProductComboBox(), amount, costPrice, total);
        list.add(item);
        obsListItens = FXCollections.observableArrayList(list);
        productEntryTableView.setItems(obsListItens);
        productEntryTableView.getSelectionModel().selectLast();
        clear();
    }
    
    private void initProductEntryTableView() {
        productTableColumn.setCellValueFactory(new PropertyValueFactory("fkProduct"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory("amount"));
        costPriceTableColumn.setCellValueFactory(new PropertyValueFactory("costPrice"));
        totalTableColumn.setCellValueFactory(new PropertyValueFactory("total"));
        actionButtonTableColumn.setStyle("-fx-alignment: CENTER;");
        actionButtonTableColumn.setCellFactory(ActionButtonTableCell.<StockEntryProduct>forTableColumn("", (StockEntryProduct p) -> {
            list.remove(p);
            obsListItens.remove(p);
            productEntryTableView.getItems().remove(p);
            return p;
        }));    
    }

    @FXML
    void save() {
        setCreated(true);
    }

    @FXML
    void clear() {
        productComboBox.valueProperty().set(null);
        amountTextField.setText("");
        costPriceTextField.setPrice(0.0);
    }

    private void tableViewListeners() {
         productEntryTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selected = (StockEntryProduct) newValue;
            }
        });
        
        productEntryTableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            final TableHeaderRow header = (TableHeaderRow) productEntryTableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
        });
    }

}
