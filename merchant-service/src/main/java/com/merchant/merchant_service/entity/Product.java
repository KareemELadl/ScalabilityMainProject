package com.merchant.merchant_service.entity;

import com.merchant.merchant_service.enums.ProductCategory;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private Integer discount = 0;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


@Column(name = "price_after_discount", precision = 10, scale = 2, nullable = false)
private BigDecimal priceAfterDiscount;

public BigDecimal getPriceAfterDiscount() {
    return priceAfterDiscount;
}

public void setPriceAfterDiscount(BigDecimal priceAfterDiscount) {
    this.priceAfterDiscount = priceAfterDiscount;
    recalculatePriceAfterDiscount();
}


    public Product() {}

    public Product(Merchant merchant, String name, String description, BigDecimal price, Integer stock,Integer discount , ProductCategory category) {
        this.merchant = merchant;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock != null ? stock : 0;
        this.discount= discount != null ? discount : 0;
        this.category = category;
        BigDecimal hundred = BigDecimal.valueOf(100);
    BigDecimal discountAmount = price.multiply(BigDecimal.valueOf(this.discount)).divide(hundred);
    this.priceAfterDiscount = price.subtract(discountAmount);

    
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getDiscount() {
        return discount;
    }
    public ProductCategory getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public void setDiscount(Integer discount) {
        this.discount = discount;
        recalculatePriceAfterDiscount();
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    private void recalculatePriceAfterDiscount() {
    if (this.price != null && this.discount != null) {
        BigDecimal hundred = BigDecimal.valueOf(100);
        BigDecimal discountAmount = this.price.multiply(BigDecimal.valueOf(this.discount)).divide(hundred);
        this.priceAfterDiscount = this.price.subtract(discountAmount);
    }
    else {
        this.priceAfterDiscount = BigDecimal.ZERO; // Default value if price or discount is null
    }
    }
}

