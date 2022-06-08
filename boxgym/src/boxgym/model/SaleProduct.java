package boxgym.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SaleProduct {
    private int saleProductId; //Identificador
    private int fkSale; //Entrada de estoque
    private int fkProduct; //Produto
    private final ObjectProperty<Integer> amount = new SimpleObjectProperty<>(); //Quantidade
    private final ObjectProperty<BigDecimal> unitPrice = new SimpleObjectProperty<>(); //Preço unitário
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em
    private final ObjectProperty<BigDecimal> subtotal = new SimpleObjectProperty<>(); //Subotal = Quantidade * Preço unitário
    
    private String tempProductName;
    
    public SaleProduct() {
        
    }
    
    //Construtor ObservableList e CREATE
    public SaleProduct(int fkSale, int fkProduct, int amount, BigDecimal unitPrice) {
        this.fkSale = fkSale;
        this.fkProduct = fkProduct;
        setAmount(amount);
        setUnitPrice(unitPrice);
        subtotal.bind(Bindings.createObjectBinding(() -> 
                getUnitPrice().multiply(BigDecimal.valueOf(getAmount()))
        ));
    }

    public int getSaleProductId() {
        return saleProductId;
    }

    public void setSaleProductId(int saleProductId) {
        this.saleProductId = saleProductId;
    }

    public int getFkSale() {
        return fkSale;
    }

    public void setFkSale(int fkSale) {
        this.fkSale = fkSale;
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
    
    public ObjectProperty<BigDecimal> unitPriceProperty() {
        return this.unitPrice;
    }

    public final BigDecimal getUnitPrice() {
        return this.unitPriceProperty().get();
    }

    public final void setUnitPrice(final BigDecimal unitPrice) {
        this.unitPriceProperty().set(unitPrice);
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
    
    public ObjectProperty<BigDecimal> subtotalProperty() {
        return this.subtotal;
    }

    public final BigDecimal getSubtotal() {
        return this.subtotalProperty().get();
    }

    public final void setSubtotal(final BigDecimal subtotal) {
        this.subtotalProperty().set(subtotal);
    }

    public String getTempProductName() {
        return tempProductName;
    }

    public void setTempProductName(String tempProductName) {
        this.tempProductName = tempProductName;
    }
    
}
