package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.dao.ProductDao;
import boxgym.dao.SaleDao;
import boxgym.dao.SaleProductDao;
import boxgym.helper.ActionButtonTableCell;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Sale;
import boxgym.model.SaleProduct;
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

public class SalesAddController implements Initializable {

    CustomerDao customerDao = new CustomerDao();
    LinkedHashMap<Integer, String> customerMap = customerDao.getCustomerForHashMap();

    ProductDao produtDao = new ProductDao();
    LinkedHashMap<Integer, String> productMap = produtDao.getProductForHashMap();

    List<SaleProduct> list = new ArrayList<>();
    ObservableList<SaleProduct> obsListItens;

    boolean productAlreadyInserted;

    AlertHelper ah = new AlertHelper();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane saleArea;

    @FXML
    private PrefixSelectionComboBox<String> customerComboBox;

    @FXML
    private DatePicker saleDateDatePicker;

    @FXML
    private Button addSaleButton;

    @FXML
    private AnchorPane productsEntryArea;

    @FXML
    private TextField saleIdTextField;

    @FXML
    private PrefixSelectionComboBox<String> productComboBox;

    @FXML
    private LimitedTextField amountTextField;

    @FXML
    private CurrencyField unitPriceTextField;

    @FXML
    private Button addProductEntryButton;

    @FXML
    private TableView<SaleProduct> productEntryTableView;

    @FXML
    private TableColumn<SaleProduct, String> productTableColumn;

    @FXML
    private TableColumn<SaleProduct, Integer> amountTableColumn;

    @FXML
    private TableColumn<SaleProduct, BigDecimal> unitPriceTableColumn;

    @FXML
    private TableColumn<SaleProduct, BigDecimal> subtotalTableColumn;

    @FXML
    private TableColumn<SaleProduct, Button> actionButtonTableColumn;

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

    private boolean saleCreationFlag;
    private boolean productsEntryCreationFlag;

    public boolean isSaleCreationFlag() {
        return saleCreationFlag;
    }

    public void setSaleCreationFlag(boolean saleCreationFlag) {
        this.saleCreationFlag = saleCreationFlag;
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

        setSaleCreationFlag(false);
        setProductsEntryCreationFlag(false);
        inputRestrictions();
        loadCustomerNameComboBox();
        saleDateDatePicker.setEditable(false);

        loadProductNameComboBox();
        productNameComboBoxListener();
        initProductEntryTableView();
        countLabel.setText("Exibindo nenhum produto");
        TextFieldFormat.currencyFormat(totalPriceTextField, BigDecimal.ZERO);
    }

    private void buttonsProperties() {
        ButtonHelper.buttonCursor(addSaleButton, addProductEntryButton, saveButton, clearButton);
        ButtonHelper.iconButton(firstRow, lastRow);
    }

    private void inputRestrictions() {
        amountTextField.setValidationPattern("[0-9]", 10);
    }

    private void loadCustomerNameComboBox() {
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (String s : customerMap.values()) {
            obsList.add(s);
        }
        customerComboBox.setPromptText("Selecione");
        customerComboBox.setItems(obsList);
    }

    private int getKeyFromCustomerComboBox() {
        int key = 0;
        for (Map.Entry<Integer, String> entry : customerMap.entrySet()) {
            if (customerComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    @FXML
    private void addSale(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.invalidComboBox(customerComboBox, "Selecione o campo Cliente. \n");
        validation.nullDatePicker(saleDateDatePicker, "Selecione o campo Data de Venda. \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível iniciar o cadastro desta venda", validation.getMessage());
        } else {
            saleArea.setDisable(true);
            productsEntryArea.setDisable(false);
            firstRow.setDisable(false);
            lastRow.setDisable(false);
            saveButton.setDisable(false);
            clearButton.setDisable(false);
            Sale sale = new Sale(getKeyFromCustomerComboBox(), saleDateDatePicker.getValue());
            SaleDao saleDao = new SaleDao();
            saleDao.create(sale);
            saleIdTextField.setText(String.valueOf(saleDao.getSaleId()));
            setSaleCreationFlag(true);
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
                unitPriceTextField.setPrice(dao.getProductCostPrice(getKeyFromProductComboBox()).doubleValue());
            }
        });
    }

    @FXML
    private void addProductEntry(ActionEvent event) {
        ProductDao productDao = new ProductDao();
        int currentProductAmount = productDao.getProductAmount(getKeyFromProductComboBox());

        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.invalidComboBox(productComboBox, "Selecione o campo Produto. \n");
        validation.emptyTextField(amountTextField.getText(), "Preencha o campo Quantidade. \n");

        if (list.stream().anyMatch(p -> p.getFkProduct() == getKeyFromProductComboBox())) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível adicionar o produto à venda", "O produto em questão já foi adicionado na lista.");
            clear();
        } else if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível adicionar o produto à venda", validation.getMessage());
        } else if (currentProductAmount != 0 && currentProductAmount < Integer.valueOf(amountTextField.getText())) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível adicionar o produto à venda", "O produto em questão possui apenas " + currentProductAmount + " unidade(s) em estoque.");
        } else if (currentProductAmount == 0) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível adicionar o produto à venda", "O produto em questão não possui nenhuma unidade em estoque.");
        } else {
            SaleProduct item = new SaleProduct(Integer.valueOf(saleIdTextField.getText()), getKeyFromProductComboBox(),
                    Integer.valueOf(amountTextField.getText()), new BigDecimal(unitPriceTextField.getPrice()));
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
        unitPriceTableColumn.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        TextFieldFormat.saleProductTableCellCurrencyFormat(unitPriceTableColumn);
        subtotalTableColumn.setCellValueFactory(new PropertyValueFactory("subtotal"));
        TextFieldFormat.saleProductTableCellCurrencyFormat(subtotalTableColumn);
        actionButtonTableColumn.setCellFactory(ActionButtonTableCell.<SaleProduct>forTableColumn("", (SaleProduct p) -> {
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
                        .map(SaleProduct::getSubtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                productEntryTableView.getItems()).asString("R$ %.2f")
        );
    }

    @FXML
    private void save(ActionEvent event) {
        if (!(list == null || list.isEmpty())) {
            AlertHelper alert = new AlertHelper();
            alert.confirmationAlert("Finalizar venda", "Deseja confirmar esta venda? Após o fechamento não será mais possível realizar alterações.");
            if (alert.getResult().get() == ButtonType.YES) {
                for (SaleProduct item : list) {
                    SaleProductDao dao = new SaleProductDao();
                    dao.create(item);
                }
                setProductsEntryCreationFlag(true);
                ah.customAlert(Alert.AlertType.INFORMATION, "Venda realizada com sucesso", "");
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
        unitPriceTextField.setPrice(0.0);
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
