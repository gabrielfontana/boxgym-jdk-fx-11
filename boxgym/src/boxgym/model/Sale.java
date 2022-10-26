package boxgym.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Sale {

    private int saleId; //Identificador
    private int fkCustomer; //Cliente
    private LocalDate saleDate; //Data de venda
    private String status; //Status
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    private String tempCustomerName;
    private final ObjectProperty<BigDecimal> tempTotal = new SimpleObjectProperty<>();

    public Sale() {

    }

    //Construtor CREATE
    public Sale(int fkCustomer, LocalDate saleDate, String status) {
        this.fkCustomer = fkCustomer;
        this.saleDate = saleDate;
        this.status = status;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getFkCustomer() {
        return fkCustomer;
    }

    public void setFkCustomer(int fkCustomer) {
        this.fkCustomer = fkCustomer;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
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

    public ObjectProperty<BigDecimal> tempTotalProperty() {
        return this.tempTotal;
    }

    public final BigDecimal getTempTotal() {
        return this.tempTotalProperty().get();
    }

    public final void setTempTotal(final BigDecimal tempTotal) {
        this.tempTotalProperty().set(tempTotal);
    }
}
