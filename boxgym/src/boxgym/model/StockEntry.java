package boxgym.model;

import java.time.LocalDate;

public class StockEntry {

    private int stockEntryId; //Identificador
    private LocalDate invoiceIssueDate; //Data de emissão da nota fiscal
    private String invoiceNumber; //Número da nota fiscal
    private int fkSupplier; //Fornecedor
    private String createdAt; //Criado em
    private String updatedAt; //Atualizado em

    @Override
    public String toString() {
        return "StockEntry{" + "invoiceIssueDate=" + invoiceIssueDate + ", invoiceNumber=" + invoiceNumber + ", fkSupplier=" + fkSupplier + '}';
    }

    
    
    public StockEntry() {

    }

    //Construtor CREATE
    public StockEntry(LocalDate invoiceIssueDate, String invoiceNumber, int fkSupplier) {
        this.invoiceIssueDate = invoiceIssueDate;
        this.invoiceNumber = invoiceNumber;
        this.fkSupplier = fkSupplier;
    }

    public int getStockEntryId() {
        return stockEntryId;
    }

    public void setStockEntryId(int stockEntryId) {
        this.stockEntryId = stockEntryId;
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

    public int getFkSupplier() {
        return fkSupplier;
    }

    public void setFkSupplier(int fkSupplier) {
        this.fkSupplier = fkSupplier;
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
