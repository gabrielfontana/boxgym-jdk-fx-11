package boxgym.controller;

import boxgym.dao.PaymentDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.ButtonHelper;
import boxgym.helper.InputMasks;
import boxgym.helper.TextValidationHelper;
import boxgym.model.Billing;
import boxgym.model.Payment;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;

public class PaymentsAddController implements Initializable {

    AlertHelper ah = new AlertHelper();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker paymentDateDatePicker;

    @FXML
    private LimitedTextField valueToBePaidTextField;

    /*@FXML
    private LimitedTextField finesAndInterestTextField;*/

    @FXML
    private Label billingValueToPayLabel;

    @FXML
    private Label valueToBePaidLabel;

    @FXML
    private Label moneyChangeText;

    @FXML
    private Label moneyChangeLabel;

    @FXML
    private Button calculateButton;

    @FXML
    private Button saveButton;

    NumberFormat nf = NumberFormat.getInstance();

    private Billing loadBilling;
    private boolean paid = false;

    public Billing getLoadBilling() {
        return loadBilling;
    }

    public void setLoadBilling(Billing loadBilling) {
        this.loadBilling = loadBilling;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPaid(false);
        ButtonHelper.buttonCursor(calculateButton, saveButton);
        initMethods();
        Platform.runLater(() -> {
            initValues();
        });
    }

    private void initMethods() {
        inputMasks();
        initPaymentDate();
        valueToBePaidTextFieldListener();
    }

    private void inputMasks() {
        InputMasks.monetaryField(valueToBePaidTextField);
        /*InputMasks.monetaryField(finesAndInterestTextField);*/
    }

    private void initPaymentDate() {
        paymentDateDatePicker.setEditable(false);
        paymentDateDatePicker.setValue(LocalDate.now());
    }

    private void initValues() {
        String value = nf.format(loadBilling.getValueToPay());
        if (String.valueOf(loadBilling.getValueToPay()).endsWith(".00")) {
            value = value.concat(",00");
        }

        valueToBePaidTextField.setText(value);
        eraseListener(valueToBePaidTextField);
        /*finesAndInterestTextField.setText("0,00");
        eraseListener(finesAndInterestTextField);*/

        billingValueToPayLabel.setText(value);
        valueToBePaidLabel.setText(value);
        moneyChangeLabel.setText("0,00");
    }

    private void eraseListener(LimitedTextField text) {
        text.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                if (text.getText().equals("0,00")) {
                    text.setText("");
                }
            }

            if (oldValue) {
                if (!(text.getText().equals("0,00")) && text.getText() != null && !(text.getText().isEmpty())) {
                    text.setText(String.valueOf(text.getText()));
                } else {
                    text.setText("0,00");
                }
            }
        });

    }

    private void valueToBePaidTextFieldListener() {
        valueToBePaidTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            valueToBePaidLabel.setText(newValue);
            moneyChangeText.setText("");
            moneyChangeLabel.setText("");
        });
    }

    /*private void finesAndInterestCalc() {
        BigDecimal billingValueToPay = new BigDecimal(replaceDotComma(nf.format(loadBilling.getValueToPay())));
        BigDecimal finesAndInterest = new BigDecimal(replaceDotComma(finesAndInterestTextField.getText()));
        BigDecimal newBillingValueToPay = billingValueToPay.add(finesAndInterest);

        if (finesAndInterestTextField.getText().equals("0,00") || billingValueToPayLabel.getText().isEmpty()) {
            billingValueToPayLabel.setText(nf.format(loadBilling.getValueToPay()));
        } else {
            billingValueToPayLabel.setText(String.valueOf(nf.format(newBillingValueToPay)));
        }
    }*/

    private void moneyChangeLabelCalc() {
        BigDecimal billingValueToPay = new BigDecimal(replaceDotComma(billingValueToPayLabel.getText()));
        BigDecimal valueToBePaid = new BigDecimal(replaceDotComma(valueToBePaidLabel.getText()));
        BigDecimal moneyChange = valueToBePaid.subtract(billingValueToPay);

        if (valueToBePaid.compareTo(billingValueToPay) > 0) {
            moneyChangeText.setText("Troco");
            moneyChangeLabel.setText(String.valueOf(nf.format(moneyChange)));
        } else if (valueToBePaid.compareTo(billingValueToPay) < 0) {
            moneyChangeText.setText("Débito");
            moneyChangeLabel.setText(String.valueOf(nf.format(moneyChange.abs())));
        } else if (valueToBePaid.compareTo(billingValueToPay) == 0) {
            moneyChangeText.setText("");
            moneyChangeLabel.setText("0,00");
        }
    }

    private String replaceDotComma(String value) {
        return value.replace(".", "").replace(",", ".");
    }

    @FXML
    private void calculate(ActionEvent event) {
        /*finesAndInterestCalc();*/
        moneyChangeLabelCalc();
    }

    @FXML
    private void save(ActionEvent event) {
        TextValidationHelper validation = new TextValidationHelper("Atenção: \n\n");
        validation.nullDatePicker(paymentDateDatePicker, "Selecione o campo Data de Recebimento. \n");

        if (!(validation.getEmptyCounter() == 0)) {
            ah.customAlert(Alert.AlertType.WARNING, "Não foi possível confirmar o pagamento desta cobrança", validation.getMessage());
        } else {
            moneyChangeLabelCalc();
            
            BigDecimal billingValueToPay = new BigDecimal(replaceDotComma(billingValueToPayLabel.getText()));
            BigDecimal valueToBePaid = new BigDecimal(replaceDotComma(valueToBePaidLabel.getText()));
            BigDecimal paidValue = new BigDecimal("0");
            if (valueToBePaid.compareTo(billingValueToPay) > 0 || valueToBePaid.compareTo(billingValueToPay) == 0) {
                paidValue = billingValueToPay; //Valor que será pago é maior ou igual que o valor da cobrança
                createPaymentObject(loadBilling.getDescription(), billingValueToPay, paidValue);
                changeBillingStatusAfterPayment(loadBilling.getBillingId());

                if (loadBilling.getFkSale() == 0) {//Se for uma mensalidade
                    changeMembershipStatusAfterPayment(loadBilling.getFkMembership());
                }
            } else if (valueToBePaid.compareTo(billingValueToPay) < 0) {
                paidValue = valueToBePaid; //Valor que será pago é menor que o valor da cobrança
                if (loadBilling.getFkSale() == 0) { //Se for uma mensalidade
                    createPaymentObject("Parte da " + loadBilling.getDescription(), billingValueToPay, paidValue);
                    changeBillingValueToPayAfterPayment(paidValue, loadBilling.getBillingId());
                } else if (loadBilling.getFkMembership() == 0) { //Se for uma venda
                    createPaymentObject("Parte de Vendas", billingValueToPay, paidValue);
                    changeBillingValueToPayAfterPayment(paidValue, loadBilling.getBillingId());
                }
            }
            setPaid(true);
            ah.customAlert(Alert.AlertType.INFORMATION, "Pagamento registrado com sucesso", "");
            anchorPane.getScene().getWindow().hide();
        }
    }

    private void createPaymentObject(String description, BigDecimal tempValueToPay, BigDecimal paidValue) {
        Payment payment = new Payment(loadBilling.getBillingId(), description, paymentDateDatePicker.getValue(), tempValueToPay, paidValue);
        PaymentDao paymentDao = new PaymentDao();
        paymentDao.create(payment);
    }

    private void changeBillingStatusAfterPayment(int billingId) {
        PaymentDao paymentDao = new PaymentDao();
        paymentDao.changeBillingStatusAfterPayment(billingId);
    }

    private void changeMembershipStatusAfterPayment(int fkMembership) {
        PaymentDao paymentDao = new PaymentDao();
        paymentDao.changeMembershipStatusAfterPayment(fkMembership);
    }

    private void changeBillingValueToPayAfterPayment(BigDecimal paidValue, int billingId) {
        PaymentDao paymentDao = new PaymentDao();
        paymentDao.changeBillingValueToPayAfterPayment(paidValue, billingId);
    }
}
