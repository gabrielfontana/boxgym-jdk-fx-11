package boxgym.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {

    private int productId; //Identificador
    private String name; //Nome
    private String category; //Categoria
    private String description; //Descrição
    private int amount; //Quantidade
    private int minimumStock; //Estoque mínimo
    private BigDecimal costPrice; //Preço de custo
    private BigDecimal sellingPrice; //Preço de venda
    private byte[] image; //Imagem
    private LocalDateTime createdAt; //Criado em
    private LocalDateTime updatedAt; //Atualizado em

    public Product() {

    }
    
    //Construtor da CombBox
    public Product(int productId, String name) {
        this.productId = productId;
        this.name = name;
    }

    //Construtor CREATE
    public Product(String name, String category, String description, int amount, int minimumStock,
            BigDecimal costPrice, BigDecimal sellingPrice, byte[] image) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.minimumStock = minimumStock;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.image = image;
    }

    //Construtor  UPDATE
    public Product(int productId, String name, String category, String description, int minimumStock,
            BigDecimal costPrice, BigDecimal sellingPrice, byte[] image) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.description = description;
        this.minimumStock = minimumStock;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.image = image;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

}
