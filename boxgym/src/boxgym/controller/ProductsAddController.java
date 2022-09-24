package boxgym.controller;

import boxgym.dao.ProductDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.ImageHelper;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Product;
import currencyfield.CurrencyField;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
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

public class ProductsAddController implements Initializable {

    ImageHelper ih = new ImageHelper();

    AlertHelper ah = new AlertHelper();

    private boolean created = false;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCreated(false);
        buttonsProperties();
        productsInputRestrictions();
        //ih.loadDefaultImage(productImage);
        productImage.setImage(new Image("boxgym/img/default-no-image.png"));
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    private void productsInputRestrictions() {
        nameTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ()%._-]", 255);
        categoryTextField.setValidationPattern("[a-zA-Z\\u00C0-\\u00FF0-9 ._-]", 255);
        amountTextField.setValidationPattern("[0-9]", 10);
        minimumStockTextField.setValidationPattern("[0-9]", 10);
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
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível realizar o cadastro deste produto!", validation.getMessage());
        } else {
            Product product = new Product(nameTextField.getText(), categoryTextField.getText(), descriptionTextArea.getText(), Integer.valueOf(amountTextField.getText()),
                    Integer.valueOf(minimumStockTextField.getText()), new BigDecimal(costPriceTextField.getPrice()), new BigDecimal(sellingPriceTextField.getPrice()),
                    ih.getImageBytes());
            ProductDao productDao = new ProductDao();
            productDao.create(product);
            setCreated(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "O produto foi cadastrado com sucesso!", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    @FXML
    void clear(ActionEvent event) {
        nameTextField.setText("");
        categoryTextField.setText("");
        descriptionTextArea.setText("");
        amountTextField.setText("");
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
