package com.pos.shared;

import com.pos.retailfeature.dao.product.Product;
import com.pos.retailfeature.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductMapper {

    public Product toEntity(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setStrength(productDto.getStrength());
        product.setCurrentSellingPrice(productDto.getSellingPrice());
        product.setDosageForm(productDto.getDosageForm());

        // dosage_form
        log.info("Dosage {} ", product.getDosageForm());

        return product;
    }

    public ProductDto toDto(Product product) {
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
}
