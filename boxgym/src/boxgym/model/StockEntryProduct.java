package boxgym.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

public class StockEntryProduct {

    private int stockEntryProductId; //Identificador
    private int fkStockEntry; //Entrada de estoque
    private int fkProduct; //Produto
    private final ObjectProperty<Integer> amount = new SimpleObjectProperty<>(); //Quantidade
    private final ObjectProperty<BigDecimal> costPrice = new SimpleObjectProperty<>(); //Preço de custo
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em
    private final ReadOnlyObjectWrapper<BigDecimal> total = new ReadOnlyObjectWrapper<>(); //Total = Quantidade * Preço de custo
    
    private String tempProductName;

    //Construtor ObservableList e CREATE
    public StockEntryProduct(int fkStockEntry, int fkProduct, int amount, BigDecimal costPrice) {
        this.fkStockEntry = fkStockEntry;
        this.fkProduct = fkProduct;
        setAmount(amount);
        setCostPrice(costPrice);
        total.bind(Bindings.createObjectBinding(() -> 
                getCostPrice().multiply(BigDecimal.valueOf(getAmount()))
        ));
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

    public ObjectProperty<Integer> amountProperty() {
        return this.amount;
    }

    public final Integer getAmount() {
        return this.amountProperty().get();
    }

    public final void setAmount(final Integer amount) {
        this.amountProperty().set(amount);
    }

    public ObjectProperty<BigDecimal> costPriceProperty() {
        return this.costPrice;
    }

    public final BigDecimal getCostPrice() {
        return this.costPriceProperty().get();
    }

    public final void setCostPrice(final BigDecimal costPrice) {
        this.costPriceProperty().set(costPrice);
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

    public ReadOnlyObjectProperty<BigDecimal> totalProperty() {
        return this.total.getReadOnlyProperty();
    }

    public final BigDecimal getTotal() {
        return this.totalProperty().get();
    }

    public String getTempProductName() {
        return tempProductName;
    }

    public void setTempProductName(String tempProductName) {
        this.tempProductName = tempProductName;
    }
    
}
