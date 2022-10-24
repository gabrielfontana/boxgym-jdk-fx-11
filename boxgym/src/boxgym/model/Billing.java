package boxgym.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Billing {

    private int billingId;
    private int fkSale;
    private int fkMembership;
    private String description;
    private LocalDate dueDate;
    private BigDecimal valueToPay;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String tempCustomerName;

    public Billing() {

    }

    //Construtor CREATE
    public Billing(String description, LocalDate dueDate, BigDecimal valueToPay, String status) {
        this.description = description;
        this.dueDate = dueDate;
        this.valueToPay = valueToPay;
        this.status = status;
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getValueToPay() {
        return valueToPay;
    }

    public void setValueToPay(BigDecimal valueToPay) {
        this.valueToPay = valueToPay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
