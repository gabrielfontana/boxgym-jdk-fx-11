package boxgym.controller;

import boxgym.dao.ProductDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.ImageHelper;
import boxgym.helper.StageHelper;
import boxgym.helper.TableViewCount;
import boxgym.helper.TextFieldFormat;
import boxgym.model.Product;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class ProductsController implements Initializable {

    AlertHelper alert = new AlertHelper();
    
    private Product selected;
    
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
    private Button minimumStockAlertButton;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;
    
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
    private Label countLabel;
    
    @FXML
    private Label selectedRowLabel;
    
    @FXML
    private MaterialDesignIconView firstRow;
    
    @FXML
    private MaterialDesignIconView lastRow;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetDetails();
        ButtonHelper.buttonCursor(filterButton, minimumStockAlertButton, addButton, updateButton, deleteButton);
        ButtonHelper.iconButton(firstRow, lastRow);
        initProductTableView();
        listeners();
        Platform.runLater(() -> searchBox.requestFocus());
        enableOrDisableMinimumStockButton();
    }
    
    private void enableOrDisableMinimumStockButton() {
        ProductDao productDao = new ProductDao();
        List<String> list = productDao.checkProductsBelowMinimumStock();
        if (list.isEmpty()) {
            minimumStockAlertButton.setDisable(true);
        } else {
            minimumStockAlertButton.setDisable(false);
        }
    }
    
    @FXML
    void minimumStockAlert(ActionEvent event) {
        ProductDao productDao = new ProductDao();
        String listString = String.join("\n", productDao.checkProductsBelowMinimumStock());
        alert.customAlert(Alert.AlertType.INFORMATION, "Produto(s) com estoque abaixo da quantidade mínima:", listString);
    }

    @FXML
    void addProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/ProductsAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            ProductsAddController controller = loader.getController();

            StageHelper.createAddOrUpdateStage("Cadastrar produto", root);

            if (controller.isCreated()) {
                refreshTableView();
                productTableView.getSelectionModel().selectLast();
            }
        } catch (IOException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void updateProduct(ActionEvent event) {
        if (selected == null) {
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um produto para atualizar", "");
        } else {
            int index = productTableView.getSelectionModel().getSelectedIndex();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/ProductsUpdate.fxml"));
                Parent root = (Parent) loader.load();
                JMetro jMetro = new JMetro(root, Style.LIGHT);

                ProductsUpdateController controller = loader.getController();
                controller.setLoadProduct(selected);

                StageHelper.createAddOrUpdateStage("Atualizar produto", root);

                if (controller.isUpdated()) {
                    refreshTableView();
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
            alert.customAlert(Alert.AlertType.WARNING, "Selecione um produto para excluir", "");
        } else {
            alert.confirmationAlert("Excluir produto", "Tem certeza que deseja excluir o produto '" + selected.getName() + "'? "
                    + "\n\nO produto será excluído de forma definitiva e não poderá ser recuperado.");
            if (alert.getResult().get() == ButtonType.YES) {
                boolean saleConstraint = productDao.checkSaleDeleteConstraint(selected.getProductId());
                boolean stockEntryConstraint = productDao.checkStockEntryDeleteConstraint(selected.getProductId());
                if (saleConstraint && stockEntryConstraint) {
                    alert.customAlert(Alert.AlertType.WARNING, "Não foi possível excluir", "Existem vendas e entradas de estoque relacionadas a este produto.");
                } else if (saleConstraint) {
                    alert.customAlert(Alert.AlertType.WARNING, "Não foi possível excluir", "Existem vendas relacionadas a este produto.");
                } else if (stockEntryConstraint) {
                    alert.customAlert(Alert.AlertType.WARNING, "Não foi possível excluir", "Existem entradas de estoque relacionadas a este produto.");
                } else {
                    productDao.delete(selected);
                    refreshTableView();
                    resetDetails();
                    alert.customAlert(Alert.AlertType.INFORMATION, "Produto excluído com sucesso", "");
                }
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
                if (selected.getImage() == null) {
                    productImageView.setImage(new Image("/boxgym/img/default-no-image.png"));
                } else {
                    productImageView.setImage(SwingFXUtils.toFXImage(ImageHelper.convertBytesToImage(selected), null));
                }
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

    private ObservableList<Product> loadData() {
        ProductDao productDao = new ProductDao();
        return FXCollections.observableArrayList(productDao.read());
    }

    private void refreshTableView() {
        productTableView.setItems(loadData());
        search();
        countLabel.setText(TableViewCount.footerMessage(productTableView.getItems().size(), "resultado"));
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

        refreshTableView();
    }

    private boolean caseSensitiveEnabled(Product product, String searchText, int optionOrder) {
        String productId = String.valueOf(product.getProductId());
        String name = product.getName();
        String category = product.getCategory();
        String amount = String.valueOf(product.getAmount());
        String minimumStock = String.valueOf(product.getMinimumStock());
        String costPrice = "R$ ".concat(String.valueOf(product.getCostPrice()).replace(".", ","));
        String sellingPrice = "R$ ".concat(String.valueOf(product.getSellingPrice()).replace(".", ","));

        List<String> fields = Arrays.asList(productId, name, category, amount, minimumStock, costPrice, sellingPrice);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean caseSensitiveDisabled(Product product, String searchText, int optionOrder) {
        String productId = String.valueOf(product.getProductId());
        String name = product.getName().toLowerCase();
        String category = product.getCategory().toLowerCase();
        String amount = String.valueOf(product.getAmount());
        String minimumStock = String.valueOf(product.getMinimumStock());
        String costPrice = "r$ ".concat(String.valueOf(product.getCostPrice()).replace(".", ","));
        String sellingPrice = "r$ ".concat(String.valueOf(product.getSellingPrice()).replace(".", ","));

        List<String> fields = Arrays.asList(productId, name, category, amount, minimumStock, costPrice, sellingPrice);

        return stringComparasion(fields, searchText, optionOrder);
    }

    private boolean stringComparasion(List<String> list, String searchText, int optionOrder) {
        boolean searchReturn = false;
        switch (optionOrder) {
            case 1:
                searchReturn = (list.get(0).contains(searchText)) || (list.get(1).contains(searchText))
                        || (list.get(2).contains(searchText)) || (list.get(3).contains(searchText))
                        || (list.get(4).contains(searchText)) || (list.get(5).contains(searchText))
                        || (list.get(6).contains(searchText));
                break;
            case 2:
                searchReturn = (list.get(0).equals(searchText)) || (list.get(1).equals(searchText))
                        || (list.get(2).equals(searchText)) || (list.get(3).equals(searchText))
                        || (list.get(4).equals(searchText)) || (list.get(5).equals(searchText))
                        || (list.get(6).equals(searchText));
                break;
            case 3:
                searchReturn = (list.get(0).startsWith(searchText)) || (list.get(1).startsWith(searchText))
                        || (list.get(2).startsWith(searchText)) || (list.get(3).startsWith(searchText))
                        || (list.get(4).startsWith(searchText)) || (list.get(5).startsWith(searchText))
                        || (list.get(6).startsWith(searchText));
                break;
            case 4:
                searchReturn = (list.get(0).endsWith(searchText)) || (list.get(1).endsWith(searchText))
                        || (list.get(2).endsWith(searchText)) || (list.get(3).endsWith(searchText))
                        || (list.get(4).endsWith(searchText)) || (list.get(5).endsWith(searchText))
                        || (list.get(6).endsWith(searchText));
                break;
            default:
                break;
        }
        return searchReturn;
    }

    private void search() {
        FilteredList<Product> filteredData = new FilteredList<>(loadData(), p -> true);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                if (caseSensitiveOp.isSelected()) {
                    if (containsOp.isSelected()) return caseSensitiveEnabled(product, newValue, 1);
                    if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveEnabled(product, newValue, 2);
                    if (startsWithOp.isSelected()) return caseSensitiveEnabled(product, newValue, 3);
                    if (endsWithOp.isSelected()) return caseSensitiveEnabled(product, newValue, 4);
                } 
                
                if (alphabeticalEqualsToOp.isSelected()) return caseSensitiveDisabled(product, newValue.toLowerCase(), 2);
                if (startsWithOp.isSelected()) return caseSensitiveDisabled(product, newValue.toLowerCase(), 3);
                if (endsWithOp.isSelected()) return caseSensitiveDisabled(product, newValue.toLowerCase(), 4);
                
                return caseSensitiveDisabled(product, newValue.toLowerCase(), 1);
            });
            productTableView.getSelectionModel().clearSelection();
            countLabel.setText(TableViewCount.footerMessage(productTableView.getItems().size(), "resultado"));
            selectedRowLabel.setText("");
        });

        SortedList<Product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productTableView.comparatorProperty());
        productTableView.setItems(sortedData);
    }

    private void listeners() {
        productTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = (Product) newValue;
            showDetails();
            if (selected == null) {
                selectedRowLabel.setText("");
            } else {
                selectedRowLabel.setText("Linha " + String.valueOf(productTableView.getSelectionModel().getSelectedIndex() + 1) + " selecionada");
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
        productTableView.scrollTo(0);
        productTableView.getSelectionModel().selectFirst();
    }

    @FXML
    void goToLastRow(MouseEvent event) {
        if (productTableView.getItems().size() == 1) {
            goToFirstRow(event);
        } else {
            productTableView.getSelectionModel().selectLast();
            productTableView.scrollTo(productTableView.getItems().size() - 1);
        }
    }

}
