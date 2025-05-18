package com.example.Transaction.Model;

import jakarta.persistence.*;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Setter
public class Product {

    /* ─────────── core columns ─────────── */
    @Setter
    @Id
    private Long id;

    @Setter
    private Long merchantId;

    @Setter
    @Column(length = 150, nullable = false)
    private String name;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private Integer discount = 0;              // %

    @Column(name = "price_after_discount",
            precision = 10, scale = 2,
            nullable = false)
    private BigDecimal priceAfterDiscount;

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    private BigDecimal calcPad() {
        if (price == null) return BigDecimal.ZERO.setScale(2);
        int pct = (discount == null) ? 0 : discount;
        if (pct == 0) return price.setScale(2, RoundingMode.HALF_UP);

        return price
                .multiply(BigDecimal.valueOf(100 - pct))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        this.priceAfterDiscount = calcPad();
    }
    public void setDiscount(Integer discount) {
        this.discount = (discount == null) ? 0 : discount;
        this.priceAfterDiscount = calcPad();
    }
    public void setStock(Integer stock) {
        this.stock = (stock == null) ? 0 : stock;
    }

    @PrePersist
    @PreUpdate
    private void syncBeforeSave() {
        this.priceAfterDiscount = calcPad();           // final safeguard
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        this.updatedAt = now;
    }

    public Product() {}

    public Product(Long merchantId, String name, String description,
                   BigDecimal price, Integer stock, Integer discount,
                   ProductCategory category) {
        this.merchantId = merchantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = (stock == null) ? 0 : stock;
        this.discount = (discount == null) ? 0 : discount;
        this.category = category;
        this.priceAfterDiscount = calcPad();
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /* getters … (keep what you already have) */

    public ProductCategory getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDiscount() {
        return discount;
    }

    public Long getId() {
        return id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

//    public void setPriceAfterDiscount(BigDecimal priceAfterDiscount) {
//        this.priceAfterDiscount = priceAfterDiscount;
//    }

    public Integer getStock() {
        return stock;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
