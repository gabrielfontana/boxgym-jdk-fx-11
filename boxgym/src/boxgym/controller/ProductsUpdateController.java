package boxgym.controller;

import boxgym.dao.ProductDao;
import boxgym.dao.SupplierDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.ImageHelper;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Product;
import currencyfield.CurrencyField;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;

public class ProductsUpdateController implements Initializable {

    SupplierDao dao = new SupplierDao();
    LinkedHashMap<Integer, String> map = dao.readId();
    ImageHelper ih = new ImageHelper();
    AlertHelper ah = new AlertHelper();
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView productImage;

    @FXML
    private LimitedTextField nameTextField;

    @FXML
    private LimitedTextField categoryTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private LimitedTextField amountTextField;

    @FXML
    private LimitedTextField minimumStockTextField;

    @FXML
    private CurrencyField costPriceTextField;

    @FXML
    private CurrencyField sellingPriceTextField;

    @FXML
    private ComboBox<String> fkSupplierComboBox;
    
    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    private Product loadProduct;

    private boolean updated = false;

    public Product getLoadProduct() {
        return loadProduct;
    }

    public void setLoadProduct(Product loadProduct) {
        this.loadProduct = loadProduct;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpdated(false);
        ButtonHelper.addOrUpdateButtons(saveButton, clearButton);
        ButtonHelper.imageButton(productImage);
        loadSupplierNameComboBox();
        productsInputRestrictions();
        ih.loadDefaultImage(productImage);
        Platform.runLater(() -> {
            initProduct();
        });
    }
    
    private void loadSupplierNameComboBox() {
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (String s : map.values()) {
            obsList.add(s);
        }
        fkSupplierComboBox.setPromptText("Selecione");
        fkSupplierComboBox.setItems(obsList);
    }
    
    private void productsInputRestrictions() {
        nameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
        categoryTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
        minimumStockTextField.setValidationPattern("[0-9]", 10);
    }
    
    private String getValueFromComboBox() {
        String fkSupplier = null;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (loadProduct.getFkSupplier() == entry.getKey()) {
                fkSupplier = entry.getValue();
                break;
            }
        }
        return fkSupplier;
    }

    private void initProduct() {
        nameTextField.setText(loadProduct.getName());
        categoryTextField.setText(loadProduct.getCategory());
        descriptionTextArea.setText(loadProduct.getDescription());
        amountTextField.setText(String.valueOf(loadProduct.getAmount()));
        minimumStockTextField.setText(String.valueOf(loadProduct.getMinimumStock()));
        costPriceTextField.setPrice(loadProduct.getCostPrice().doubleValue());
        sellingPriceTextField.setPrice(loadProduct.getSellingPrice().doubleValue());
        fkSupplierComboBox.valueProperty().set(getValueFromComboBox());
        try{
            productImage.setImage(SwingFXUtils.toFXImage(ImageHelper.convertBytesToImage(loadProduct), null));
        } catch (IOException ex) {
            Logger.getLogger(ProductsUpdateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int getKeyFromComboBox() {
        int fkSupplier = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (fkSupplierComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                fkSupplier = entry.getKey();
                break;
            }
        }
        return fkSupplier;
    }
    
    @FXML
    void chooseImage(MouseEvent event) {
        ih.chooser(productImage);
    }

    @FXML
    void save(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper();
        validation.handleEmptyField(nameTextField.getText(), "'Nome'\n");
        validation.handleEmptyField(amountTextField.getText(), "'Quantidade'\n");
        validation.handleEmptyField(minimumStockTextField.getText(), "'Estoque Mínimo'\n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível editar o cadastro deste produto!", validation.getMessage());
        } else if (fkSupplierComboBox.getItems().size() <= 0) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível editar o cadastro deste produto!", "Cadastre um fornecedor antes.");
        } else if (fkSupplierComboBox.getSelectionModel().getSelectedItem() == null) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível editar o cadastro deste produto!", "Escolha um fornecedor!");
        } else {
            Product product = new Product(loadProduct.getProductId(), nameTextField.getText(), categoryTextField.getText(), descriptionTextArea.getText(), 
                    Integer.valueOf(minimumStockTextField.getText()), new BigDecimal(costPriceTextField.getPrice()), new BigDecimal(sellingPriceTextField.getPrice()), 
                    ih.getImageBytes(), getKeyFromComboBox());
            ProductDao productDao = new ProductDao();
            productDao.update(product);
            setUpdated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "O produto foi editado com sucesso!", "");
            anchorPane.getScene().getWindow().hide();
        }
    }
    
    @FXML
    void clear(ActionEvent event) {
        nameTextField.setText("");
        categoryTextField.setText("");
        descriptionTextArea.setText("");
        minimumStockTextField.setText("");
        costPriceTextField.setPrice(0.0);
        sellingPriceTextField.setPrice(0.0);
        fkSupplierComboBox.valueProperty().set(null);
        ih.loadDefaultImage(productImage);
    }

}
