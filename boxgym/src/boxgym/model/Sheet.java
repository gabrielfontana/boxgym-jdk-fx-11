package boxgym.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Sheet {

    private int sheetId; //Identificador
    private int fkCustomer; //Cliente
    private String description; //Descrição
    private LocalDate expirationDate; //Data de validade
    private String comments; //Observações
    private String status; //Status
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    private String tempCustomerName;

    public Sheet() {

    }
    
    //Construtor CREATE
    public Sheet(int fkCustomer, String description, LocalDate expirationDate, String comments, String status) {
        this.fkCustomer = fkCustomer;
        this.description = description;
        this.expirationDate = expirationDate;
        this.comments = comments;
        this.status = status;
    }
    
    public int getSheetId() {
        return sheetId;
    }

    public void setSheetId(int sheetId) {
        this.sheetId = sheetId;
    }

    public int getFkCustomer() {
        return fkCustomer;
    }

    public void setFkCustomer(int fkCustomer) {
        this.fkCustomer = fkCustomer;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
