package boxgym.controller;

import boxgym.dao.ProductDao;
import boxgym.dao.StockEntryDao;
import boxgym.dao.StockEntryProductDao;
import boxgym.dao.SupplierDao;
import boxgym.helper.ActionButtonTableCell;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.helper.TextValidationHelper;
import boxgym.model.StockEntry;
import boxgym.model.StockEntryProduct;
import currencyfield.CurrencyField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;
import org.controlsfx.control.PrefixSelectionComboBox;

public class StockEntryAddController implements Initializable {

    SupplierDao supplierDao = new SupplierDao();
    LinkedHashMap<Integer, String> supplierMap = supplierDao.getSupplierForHashMap();

    ProductDao produtDao = new ProductDao();
    LinkedHashMap<Integer, String> productMap = produtDao.getProductForHashMap();

    List<StockEntryProduct> list = new ArrayList<>();
    ObservableList<StockEntryProduct> obsListItens;

    AlertHelper ah = new AlertHelper();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane stockEntryArea;

    @FXML
    private PrefixSelectionComboBox<String> supplierComboBox;

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
    private PrefixSelectionComboBox<String> productComboBox;

    @FXML
    private LimitedTextField amountTextField;

    @FXML
    private CurrencyField costPriceTextField;

    @FXML
    private Button addProductEntryButton;

    @FXML
    private TableView<StockEntryProduct> productEntryTableView;

    @FXML
    private TableColumn<StockEntryProduct, String> productTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, Integer> amountTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, BigDecimal> costPriceTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, BigDecimal> subtotalTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, Button> actionButtonTableColumn;

    @FXML
    private Label countLabel;

    @FXML
    private MaterialDesignIconView firstRow;

    @FXML
    private MaterialDesignIconView lastRow;

    @FXML
    private LimitedTextField totalPriceTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    private boolean stockEntryCreationFlag;
    private boolean productsEntryCreationFlag;

    public boolean isStockEntryCreationFlag() {
        return stockEntryCreationFlag;
    }

    public void setStockEntryCreationFlag(boolean stockEntryCreationFlag) {
        this.stockEntryCreationFlag = stockEntryCreationFlag;
    }

    public boolean isProductsEntryCreationFlag() {
        return productsEntryCreationFlag;
    }

    public void setProductsEntryCreationFlag(boolean productsEntryCreationFlag) {
        this.productsEntryCreationFlag = productsEntryCreationFlag;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonsProperties();

        setStockEntryCreationFlag(false);
        setProductsEntryCreationFlag(false);
        inputRestrictions();
        loadSupplierNameComboBox();
        invoiceIssueDateDatePicker.setEditable(false);

        loadProductNameComboBox();
        productNameComboBoxListener();
        initProductEntryTableView();
        countLabel.setText("Exibindo nenhum produto");
        TextFieldFormat.currencyFormat(totalPriceTextField, BigDecimal.ZERO);
    }

    private void buttonsProperties() {
        ButtonHelper.buttonCursor(addStockEntryButton, addProductEntryButton, saveButton, clearButton);
        ButtonHelper.iconButton(firstRow, lastRow);
    }

    private void inputRestrictions() {
        invoiceNumberTextField.setValidationPattern("[0-9]", 10);
        amountTextField.setValidationPattern("[0-9]", 10);
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

    @FXML
    void addStockEntry(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.invalidComboBox(supplierComboBox, "Selecione o campo Fornecedor. \n");
        validation.nullDatePicker(invoiceIssueDateDatePicker, "Selecione o campo Data de Emissão. \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível iniciar o cadastro desta entrada de estoque", validation.getMessage());
        } else {
            stockEntryArea.setDisable(true);
            productsEntryArea.setDisable(false);
            firstRow.setDisable(false);
            lastRow.setDisable(false);
            saveButton.setDisable(false);
            clearButton.setDisable(false);
            StockEntry stockEntry = new StockEntry(getKeyFromSupplierComboBox(), invoiceIssueDateDatePicker.getValue(), invoiceNumberTextField.getText());
            StockEntryDao stockEntryDao = new StockEntryDao();
            stockEntryDao.create(stockEntry);
            stockEntryIdTextField.setText(String.valueOf(stockEntryDao.getStockEntryId()));
            setStockEntryCreationFlag(true);
        }
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
    void addProductEntry(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.invalidComboBox(productComboBox, "Selecione o campo Produto. \n");
        validation.emptyTextField(amountTextField.getText(), "Preencha o campo Quantidade. \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível adicionar o produto à entrada de estoque", validation.getMessage());
        } else {
            StockEntryProduct item = new StockEntryProduct(Integer.valueOf(stockEntryIdTextField.getText()), getKeyFromProductComboBox(),
                    Integer.valueOf(amountTextField.getText()), new BigDecimal(costPriceTextField.getPrice()));
            item.setTempProductName(productComboBox.getSelectionModel().getSelectedItem());
            list.add(item);
            obsListItens = FXCollections.observableArrayList(list);
            productEntryTableView.setItems(obsListItens);
            productEntryTableView.getSelectionModel().selectLast();
            initCount();
            textFieldBinding();
            clear();
        }
    }

    private void initProductEntryTableView() {
        productTableColumn.setCellValueFactory(new PropertyValueFactory("tempProductName"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory("amount"));
        costPriceTableColumn.setCellValueFactory(new PropertyValueFactory("costPrice"));
        TextFieldFormat.stockEntryProductTableCellCurrencyFormat(costPriceTableColumn);
        subtotalTableColumn.setCellValueFactory(new PropertyValueFactory("subtotal"));
        TextFieldFormat.stockEntryProductTableCellCurrencyFormat(subtotalTableColumn);
        actionButtonTableColumn.setCellFactory(ActionButtonTableCell.<StockEntryProduct>forTableColumn("", (StockEntryProduct p) -> {
            list.remove(p);
            obsListItens.remove(p);
            productEntryTableView.getItems().remove(p);
            initCount();
            return p;
        }));
    }

    private void initCount() {
        int count = obsListItens.size();
        countLabel.setText(TableViewCount.footerMessage(count, "produto"));
    }

    private void textFieldBinding() {
        totalPriceTextField.textProperty().bind(Bindings.createObjectBinding(()
                -> productEntryTableView.getItems().stream()
                        .map(StockEntryProduct::getSubtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                productEntryTableView.getItems()).asString("R$ %.2f")
        );
    }

    @FXML
    void save() {
        if (!(list == null || list.isEmpty())) {
            AlertHelper alert = new AlertHelper();
            alert.confirmationAlert("Finalizar entrada de estoque", "Deseja confirmar esta entrada de estoque? Após o fechamento não será mais possível realizar alterações.");
            if (alert.getResult().get() == ButtonType.YES) {
                for (StockEntryProduct item : list) {
                    StockEntryProductDao dao = new StockEntryProductDao();
                    dao.create(item);
                }
                setProductsEntryCreationFlag(true);
                ah.customAlert(Alert.AlertType.INFORMATION, "Entrada de estoque realizada com sucesso", "");
                anchorPane.getScene().getWindow().hide();
            }
        } else {
            ah.customAlert(Alert.AlertType.INFORMATION, "Lista de produtos vazia", "");
        }
    }

    @FXML
    void clear() {
        productComboBox.valueProperty().set(null);
        amountTextField.setText("");
        costPriceTextField.setPrice(0.0);
    }

    @FXML
    void goToFirstRow(MouseEvent event) {
        productEntryTableView.scrollTo(0);
        productEntryTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (productEntryTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            productEntryTableView.getSelectionModel().selectLast();
            productEntryTableView.scrollTo(productEntryTableView.getItems().size() - 1);
        }
    }

}
