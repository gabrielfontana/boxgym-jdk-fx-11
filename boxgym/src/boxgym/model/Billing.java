package boxgym.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Billing {

    private int billingId;
    private int fkSale;
    private int fkMembership;
    private String description;
    private LocalDate expirationDate;
    private BigDecimal valueToPay;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String tempCustomerName;

    public Billing() {

    }

    //Construtor CREATE
    public Billing(int fkSale, String description, LocalDate expirationDate, BigDecimal valueToPay) {
        this.fkSale = fkSale;
        this.description = description;
        this.expirationDate = expirationDate;
        this.valueToPay = valueToPay;
    }

    public int getBillingId() {
        return billingId;
    }

    public void setBillingId(int billingId) {
        this.billingId = billingId;
    }

    public int getFkSale() {
        return fkSale;
    }

    public void setFkSale(int fkSale) {
        this.fkSale = fkSale;
    }

    public int getFkMembership() {
        return fkMembership;
    }

    public void setFkMembership(int fkMembership) {
        this.fkMembership = fkMembership;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getValueToPay() {
        return valueToPay;
    }

    public void setValueToPay(BigDecimal valueToPay) {
        this.valueToPay = valueToPay;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTempCustomerName() {
        return tempCustomerName;
    }

    public void setTempCustomerName(String tempCustomerName) {
        this.tempCustomerName = tempCustomerName;
    }

}
