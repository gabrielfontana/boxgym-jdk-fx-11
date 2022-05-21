package boxgym.controller;

import boxgym.dao.ProductDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.ImageHelper;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Product;
import currencyfield.CurrencyField;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;

public class ProductsUpdateController implements Initializable {

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
        buttonsProperties();
        productsInputRestrictions();
        
        Platform.runLater(() -> {
            initProduct();
            ih.setImageBytes(loadProduct.getImage());
        });
    }
    
    private void productsInputRestrictions() {
        nameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ()%._-]", 255);
        categoryTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
        minimumStockTextField.setValidationPattern("[0-9]", 10);
    }
    
    private void initProduct() {
        nameTextField.setText(loadProduct.getName());
        categoryTextField.setText(loadProduct.getCategory());
        descriptionTextArea.setText(loadProduct.getDescription());
        amountTextField.setText(String.valueOf(loadProduct.getAmount()));
        minimumStockTextField.setText(String.valueOf(loadProduct.getMinimumStock()));
        costPriceTextField.setPrice(loadProduct.getCostPrice().doubleValue());
        sellingPriceTextField.setPrice(loadProduct.getSellingPrice().doubleValue());
        try{
            if(loadProduct.getImage() == null) {
                productImage.setImage(new Image("boxgym/img/default-no-image.png"));
            } else {
                productImage.setImage(SwingFXUtils.toFXImage(ImageHelper.convertBytesToImage(loadProduct), null));
            }
        } catch (IOException ex) {
            Logger.getLogger(ProductsUpdateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void chooseImage(MouseEvent event) {
        ih.chooser(productImage);
    }

    @FXML
    void save(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Por favor, preencha o(s) seguinte(s) campo(s) obrigatório(s): \n\n");
        validation.emptyTextField(nameTextField.getText(), "'Nome'\n");
        validation.emptyTextField(amountTextField.getText(), "'Estoque Inicial'\n");
        validation.emptyTextField(minimumStockTextField.getText(), "'Estoque Mínimo'\n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível atualizar o cadastro deste produto!", validation.getMessage());
        } else {
            Product product = new Product(loadProduct.getProductId(), nameTextField.getText(), categoryTextField.getText(), descriptionTextArea.getText(), 
                    Integer.valueOf(minimumStockTextField.getText()), new BigDecimal(costPriceTextField.getPrice()), new BigDecimal(sellingPriceTextField.getPrice()), 
                    ih.getImageBytes());
            ProductDao productDao = new ProductDao();
            productDao.update(product);
            setUpdated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "O produto foi atualizado com sucesso!", "");
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
        //ih.loadDefaultImage(productImage);
        productImage.setImage(new Image("boxgym/img/default-no-image.png"));
        ih.setImageBytes(null);
    }
    
    private void buttonsProperties() {
        ButtonHelper.buttonCursor(saveButton, clearButton);
        ButtonHelper.imageButton(productImage);
    }
}
