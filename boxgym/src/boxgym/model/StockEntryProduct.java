package boxgym.model;

import java.math.BigDecimal;

public class StockEntryProduct {

    private int stockEntryProductId; //Identificador
    private int fkStockEntry; //Entrada de estoque
    private int fkProduct; //Produto
    private int amount; //Quantidade
    private BigDecimal costPrice; //Preço de custo
    private BigDecimal total; //Total = Quantidade * Preço de custo

    public StockEntryProduct() {

    }

    //Construtor CREATE
    public StockEntryProduct(int fkStockEntry, int fkProduct, int amount, BigDecimal costPrice) {
        this.fkStockEntry = fkStockEntry;
        this.fkProduct = fkProduct;
        this.amount = amount;
        this.costPrice = costPrice;
    }
    
    public int getStockEntryProductId() {
        return stockEntryProductId;
    }

    public void setStockEntryProductId(int stockEntryProductId) {
        this.stockEntryProductId = stockEntryProductId;
    }

    public int getFkStockEntry() {
        return fkStockEntry;
    }

    public void setFkStockEntry(int fkStockEntry) {
        this.fkStockEntry = fkStockEntry;
    }

    public int getFkProduct() {
        return fkProduct;
    }

    public void setFkProduct(int fkProduct) {
        this.fkProduct = fkProduct;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
