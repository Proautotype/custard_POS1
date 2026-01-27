package com.pos.inventoryfeature;

import com.pos.retailfeature.dto.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class StockEntryDto {
    private Long id;
    private ProductDto product;

    private Integer quantityReceived;
    private Integer quantitySoldFromThisBatch = 0;

    private LocalDate arrivalDate;
    private LocalDate expiryDate;

    private String batchNumber; // e.g., "LOT-2024-X"

    private BigDecimal costPrice;       // What you paid the supplier for THIS batch
    private BigDecimal totalBatchCost;      // unitCostPrice * quantityReceived

    private String description;

    private String supplierId;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public Integer getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public Integer getQuantitySoldFromThisBatch() {
        return quantitySoldFromThisBatch;
    }

    public void setQuantitySoldFromThisBatch(Integer quantitySoldFromThisBatch) {
        this.quantitySoldFromThisBatch = quantitySoldFromThisBatch;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getTotalBatchCost() {
        return totalBatchCost;
    }

    public void setTotalBatchCost(BigDecimal totalBatchCost) {
        this.totalBatchCost = totalBatchCost;
    }
}
