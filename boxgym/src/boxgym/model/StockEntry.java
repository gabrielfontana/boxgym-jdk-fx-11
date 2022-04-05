package boxgym.model;

import java.time.LocalDate;

public class StockEntry {

    private int stockEntryId; //Identificador
    private int fkSupplier; //Fornecedor
    private LocalDate invoiceIssueDate; //Data de emissão da nota fiscal
    private String invoiceNumber; //Número da nota fiscal
    private String createdAt; //Criado em
    private String updatedAt; //Atualizado em

    public StockEntry() {

    }

    //Construtor CREATE
    public StockEntry(int fkSupplier, LocalDate invoiceIssueDate, String invoiceNumber) {
        this.fkSupplier = fkSupplier;
        this.invoiceIssueDate = invoiceIssueDate;
        this.invoiceNumber = invoiceNumber;
    }

    public int getStockEntryId() {
        return stockEntryId;
    }

    public void setStockEntryId(int stockEntryId) {
        this.stockEntryId = stockEntryId;
    }
    
    public int getFkSupplier() {
        return fkSupplier;
    }

    public void setFkSupplier(int fkSupplier) {
        this.fkSupplier = fkSupplier;
    }

    public LocalDate getInvoiceIssueDate() {
        return invoiceIssueDate;
    }

    public void setInvoiceIssueDate(LocalDate invoiceIssueDate) {
        this.invoiceIssueDate = invoiceIssueDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
