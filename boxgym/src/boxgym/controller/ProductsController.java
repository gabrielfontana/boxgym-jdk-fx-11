package boxgym.controller;

import boxgym.dao.ProductDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.ImageHelper;
import boxgym.helper.StageHelper;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Product;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class ProductsController implements Initializable {

    AlertHelper ah = new AlertHelper();

    @FXML
    private TextField searchBox;

    @FXML
    private MenuButton exportButton;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, Integer> productIdTableColumn;

    @FXML
    private TableColumn<Product, String> nameTableColumn;

    @FXML
    private TableColumn<Product, String> categoryTableColumn;

    @FXML
    private TableColumn<Product, Integer> amountTableColumn;

    @FXML
    private TableColumn<Product, Integer> minimumStockTableColumn;

    @FXML
    private TableColumn<Product, BigDecimal> costPriceTableColumn;

    @FXML
    private TableColumn<Product, BigDecimal> sellingPriceTableColumn;

    @FXML
    private ImageView productImageView;

    @FXML
    private Label productIdLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private Label minimumStockLabel;

    @FXML
    private Label costPriceLabel;

    @FXML
    private Label sellingPriceLabel;

    @FXML
    private Label createdAtLabel;

    @FXML
    private Label updatedAtLabel;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private Product selected;

    AlertHelper alert = new AlertHelper();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(exportButton, addButton, updateButton, deleteButton);
        initProductTableView();
        tableViewListeners();
        searchBox.setOnKeyPressed((KeyEvent e) -> search());
    }

    @FXML
    void addProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/ProductsAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            ProductsAddController controller = loader.getController();

            StageHelper.createAddOrUpdateStage("Adicionando Produto", root);

            if (controller.isCreated()) {
                initProductTableView();
                productTableView.getSelectionModel().selectLast();
            }
        } catch (IOException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void updateProduct(ActionEvent event) {
        if (selected == null) {
            ah.customAlert(Alert.AlertType.WARNING, "Selecione um produto para editar!", "");
        } else {
            int index = productTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/ProductsUpdate.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                ProductsUpdateController controller = loader.getController();
                controller.setLoadProduct(selected);

                StageHelper.createAddOrUpdateStage("Editando Produto", root);

                if (controller.isUpdated()) {
                    initProductTableView();
                    productTableView.getSelectionModel().select(index);
                }
            } catch (IOException ex) {
                Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void deleteProduct(ActionEvent event) {
        ProductDao productDao = new ProductDao();

        if (selected == null) {
            ah.customAlert(Alert.AlertType.WARNING, "Selecione um produto para excluir!", "");
        } else {
            alert.confirmationAlert("Tem certeza que deseja excluir o produto '" + selected.getName() + "'?", "Esta ação é irreversível!");
            if (alert.getResult().get() == ButtonType.YES) {
                productDao.delete(selected);
                productTableView.setItems(loadData());
                resetDetails();
                ah.customAlert(Alert.AlertType.INFORMATION, "O produto foi excluído com sucesso!", "");
            }
        }
    }

    private void resetDetails() {
        if (selected == null) {
            productImageView.setImage(new Image("/boxgym/img/default-no-image.png"));
            productIdLabel.setText("");
            nameLabel.setText("");
            categoryLabel.setText("");
            descriptionLabel.setText("");
            amountLabel.setText("");
            minimumStockLabel.setText("");
            costPriceLabel.setText("");
            sellingPriceLabel.setText("");
            createdAtLabel.setText("");
            updatedAtLabel.setText("");
        }
    }

    private void showDetails() {
        if (selected != null) {
            try {
                productImageView.setImage(SwingFXUtils.toFXImage(ImageHelper.convertBytesToImage(selected), null));
            } catch (IOException ex) {
                Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            productIdLabel.setText(String.valueOf(selected.getProductId()));
            nameLabel.setText(selected.getName());
            categoryLabel.setText(selected.getCategory());
            descriptionLabel.setText(selected.getDescription());
            amountLabel.setText(String.valueOf(selected.getAmount()));
            minimumStockLabel.setText(String.valueOf(selected.getMinimumStock()));
            TextFieldFormat.currencyFormat(costPriceLabel, selected.getCostPrice());
            TextFieldFormat.currencyFormat(sellingPriceLabel, selected.getSellingPrice());
            createdAtLabel.setText(selected.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            updatedAtLabel.setText(selected.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }
    }

    private void initProductTableView() {
        productIdTableColumn.setCellValueFactory(new PropertyValueFactory("productId"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        categoryTableColumn.setCellValueFactory(new PropertyValueFactory("category"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory("amount"));
        minimumStockTableColumn.setCellValueFactory(new PropertyValueFactory("minimumStock"));

        costPriceTableColumn.setCellValueFactory(new PropertyValueFactory("costPrice"));
        TextFieldFormat.productTableCellCurrencyFormat(costPriceTableColumn);

        sellingPriceTableColumn.setCellValueFactory(new PropertyValueFactory("sellingPrice"));
        TextFieldFormat.productTableCellCurrencyFormat(sellingPriceTableColumn);

        productTableView.setItems(loadData());
    }

    private ObservableList<Product> loadData() {
        ProductDao productDao = new ProductDao();
        return FXCollections.observableArrayList(productDao.read());
    }

    private boolean searchFindsProduct(Product product, String searchText) {
        String productId = String.valueOf(product.getProductId());
        String name = String.valueOf(product.getName()).toLowerCase();
        String category = String.valueOf(product.getCategory()).toLowerCase();
        String description = String.valueOf(product.getDescription()).toLowerCase();
        String amount = String.valueOf(product.getAmount());
        String minimumStock = String.valueOf(product.getMinimumStock());
        String costPrice = String.valueOf(product.getCostPrice());
        String sellingPrice = String.valueOf(product.getSellingPrice());

        return (productId.contains(searchText)) || (name.contains(searchText))
                || (category.contains(searchText)) || (description.contains(searchText))
                || (amount.contains(searchText)) || (minimumStock.contains(searchText))
                || (costPrice.contains(searchText)) || (sellingPrice.contains(searchText));
    }

    private void search() {
        FilteredList<Product> filteredData = new FilteredList<>(loadData(), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return searchFindsProduct(product, newValue.toLowerCase());
            });
        });

        SortedList<Product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productTableView.comparatorProperty());
        productTableView.setItems(sortedData);
    }

    private void tableViewListeners() {
        productTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selected = (Product) newValue;
                showDetails();
            }
        });

        productTableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            final TableHeaderRow header = (TableHeaderRow) productTableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
        });
    }

    @FXML
    void exportToExcel(ActionEvent event) {

    }

    @FXML
    void generatePdf(ActionEvent event) {

    }

}
