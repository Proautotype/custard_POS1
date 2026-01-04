package com.pos.retailfeature.dto;

import com.pos.retailfeature.dao.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ViewGenericProductDto {
    private String id;
    private String name;
    private boolean isPharmaceutical = false;
    private boolean requiresPrescription = false;
    private String warning;
    private String category;
    private List<ProductDto> brands;
}
