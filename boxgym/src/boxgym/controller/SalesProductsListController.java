package boxgym.controller;

import boxgym.dao.SaleProductDao;
import boxgym.helper.ButtonHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.SaleProduct;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
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
import javafx.scene.input.MouseEvent;
import limitedtextfield.LimitedTextField;

public class SalesProductsListController implements Initializable {

    private int selectedSale;
    
    private SaleProduct selected;
    
    @FXML
    private TableView<SaleProduct> productsListTableView;
    
    @FXML
    private TableColumn<SaleProduct, String> productTableColumn;
    
    @FXML
    private TableColumn<SaleProduct, Integer> amountTableColumn;
    
    @FXML
    private TableColumn<SaleProduct, BigDecimal> unitPriceTableColumn;
    
    @FXML
    private TableColumn<SaleProduct, BigDecimal> subtotalTableColumn;
    
    @FXML
    private LimitedTextField totalPriceTextField;
    
    @FXML
    private Label countLabel;
    
    @FXML
    private Label selectedRowLabel;
    
    @FXML
    private MaterialDesignIconView firstRow;
    
    @FXML
    private MaterialDesignIconView lastRow;

    public int getSelectedSale() {
        return selectedSale;
    }

    public void setSelectedSale(int selectedSale) {
        this.selectedSale = selectedSale;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ButtonHelper.iconButton(firstRow, lastRow);
        Platform.runLater(() -> initMethods());
    }
    
    private void initMethods() {
        initProductsListTableView();
        listeners();
        TextFieldFormat.currencyFormat(totalPriceTextField, total());
        countLabel.setText(TableViewCount.footerMessage(productsListTableView.getItems().size(), "produto"));
    }

    private ObservableList<SaleProduct> loadData() {
        SaleProductDao dao = new SaleProductDao();
        return FXCollections.observableArrayList(dao.read(getSelectedSale()));
    }

    private void initProductsListTableView() {
        productTableColumn.setCellValueFactory(new PropertyValueFactory("tempProductName"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory("amount"));
        unitPriceTableColumn.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        TextFieldFormat.productTableCellCurrencyFormat(unitPriceTableColumn);
        subtotalTableColumn.setCellValueFactory(new PropertyValueFactory("subtotal"));
        TextFieldFormat.productTableCellCurrencyFormat(subtotalTableColumn);
        productsListTableView.setItems(loadData());
    }
    
    private void listeners() {
        productsListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (SaleProduct) newValue;
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(productsListTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
            }
        });
    }
    
    private BigDecimal total() {
        BigDecimal total = new BigDecimal("0");
        for (SaleProduct sp : productsListTableView.getItems()) {
            total = total.add(sp.getSubtotal());
        }
        return total;
    }

    @FXML
    void goToFirstRow(MouseEvent event) {
        productsListTableView.scrollTo(0);
        productsListTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (productsListTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            productsListTableView.getSelectionModel().selectLast();
            productsListTableView.scrollTo(productsListTableView.getItems().size() - 1);
        }
    }
    
}
