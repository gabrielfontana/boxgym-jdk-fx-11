package boxgym.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Membership {

    private int membershipId; //Identificador
    private int fkCustomer; //Cliente
    private LocalDate dueDate; //Data de vencimento
    private BigDecimal price; //Preço
    private String status; //Status
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    private String tempCustomerName;

    public Membership() {
    
    }
    
    //Construtor CREATE
    public Membership(int fkCustomer, LocalDate dueDate, BigDecimal price, String status) {
        this.fkCustomer = fkCustomer;
        this.dueDate = dueDate;
        this.price = price;
        this.status = status;
    }
    
     //Construtor UPDATE
    public Membership(int membershipId, LocalDate dueDate, BigDecimal price) {
        this.membershipId = membershipId;
        this.dueDate = dueDate;
        this.price = price;
    }
    
    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public int getFkCustomer() {
        return fkCustomer;
    }

    public void setFkCustomer(int fkCustomer) {
        this.fkCustomer = fkCustomer;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
