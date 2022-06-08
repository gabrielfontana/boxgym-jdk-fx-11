package boxgym.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Sale {
    private int saleId; //Identificador
    private int fkCustomer; //Cliente
    private LocalDate saleDate; //Data de venda
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em
    
    private String tempCustomerName;
    
    public Sale() {
        
    }
    
    //Construtor CREATE
    public Sale(int fkCustomer, LocalDate saleDate) {
        this.fkCustomer = fkCustomer;
        this.saleDate = saleDate;
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
