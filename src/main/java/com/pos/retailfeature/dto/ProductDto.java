package com.pos.retailfeature.dto;

import com.pos.retailfeature.dao.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class ProductDto {
    private String id;
    private final String name;
    private final String description;
    private final String productGenericId;
    private final String dosageForm;
    private final String strength;
    private final BigDecimal sellingPrice;

    // You might want to add a static factory method to convert from entity to DTO
    // id,name,description,generic_id,dosage_form,strength,current_selling_price

    public ProductDto(String name, String description, String productGenericId, String dosageForm, String strength, BigDecimal sellingPrice){
        this.name = name;
        this.description = description;
        this.productGenericId = productGenericId;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.sellingPrice = sellingPrice;
    }
    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .productGenericId(product.getGenericProduct() != null ? product.getGenericProduct().getId() : null)
                .dosageForm(product.getDosageForm())
                .strength(product.getStrength())
                .sellingPrice(product.getCurrentSellingPrice())
                .build();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProductGenericId() {
        return productGenericId;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productGenericId='" + productGenericId + '\'' +
                ", dosageForm='" + dosageForm + '\'' +
                ", strength='" + strength + '\'' +
                ", sellingPrice=" + sellingPrice +
                '}';
    }
}