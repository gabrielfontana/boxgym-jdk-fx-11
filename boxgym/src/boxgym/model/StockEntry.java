package boxgym.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class StockEntry {

    private int stockEntryId; //Identificador
    private int fkSupplier; //Fornecedor
    private LocalDate invoiceIssueDate; //Data de emissão da nota fiscal
    private String invoiceNumber; //Número da nota fiscal
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    private String tempSupplierName;
    private final ObjectProperty<BigDecimal> tempTotal = new SimpleObjectProperty<>();

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

    public String getTempSupplierName() {
        return tempSupplierName;
    }

    public void setTempSupplierName(String tempSupplierName) {
        this.tempSupplierName = tempSupplierName;
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
