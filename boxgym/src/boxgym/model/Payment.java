package boxgym.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {

    private int paymentId;
    private int fkBilling;
    private String description;
    private LocalDate paymentDate;
    private BigDecimal tempValueToPay;
    private BigDecimal paidValue;
    
    private String tempCustomerName;
    
    public Payment() {
        
    }

    //Construtor CREATE
    public Payment(int fkBilling, String description, LocalDate paymentDate, BigDecimal tempValueToPay, BigDecimal paidValue) {
        this.fkBilling = fkBilling;
        this.description = description;
        this.paymentDate = paymentDate;
        this.tempValueToPay = tempValueToPay;
        this.paidValue = paidValue;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getFkBilling() {
        return fkBilling;
    }

    public void setFkBilling(int fkBilling) {
        this.fkBilling = fkBilling;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getTempValueToPay() {
        return tempValueToPay;
    }

    public void setTempValueToPay(BigDecimal tempValueToPay) {
        this.tempValueToPay = tempValueToPay;
    }

    public BigDecimal getPaidValue() {
        return paidValue;
    }

    public void setPaidValue(BigDecimal paidValue) {
        this.paidValue = paidValue;
    }

    public String getTempCustomerName() {
        return tempCustomerName;
    }

    public void setTempCustomerName(String tempCustomerName) {
        this.tempCustomerName = tempCustomerName;
    }
    
}
