package com.webprj.ecom.tests.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DBOrderItem Model Class
 * This class represents the 'order_items' table structure from the database (ecom_test_db).
 * Used for DB validation in Rest Assured + Database tests.
 */
public class DBOrderItem
{
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal itemPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default Constructor
    public DBOrderItem() 
    {
    	
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getItemPrice() { return itemPrice; }
    public void setItemPrice(BigDecimal itemPrice) { this.itemPrice = itemPrice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString()
    {
        return "DBOrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                '}';
    }
}