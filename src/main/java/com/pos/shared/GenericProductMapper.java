package com.pos.shared;

import com.pos.retailfeature.dao.product.GenericProduct;
import com.pos.retailfeature.dto.CreateGenericProductDto;
import com.pos.retailfeature.dto.ViewGenericProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenericProductMapper {

    public ViewGenericProductDto toDto(GenericProduct product) {
        return ViewGenericProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .warning(product.getWarning())
                .isPharmaceutical(product.isPharmaceutical())
                .requiresPrescription(product.isRequiresPrescription())
                .build();
    }

    public GenericProduct toEntity(CreateGenericProductDto createGenericProductDto) {
        GenericProduct genericProduct = new GenericProduct();
        genericProduct.setName(createGenericProductDto.getName());
        genericProduct.setCategory(createGenericProductDto.getCategory());
        genericProduct.setPharmaceutical(createGenericProductDto.isPharmaceutical());
        genericProduct.setWarning(createGenericProductDto.getWarning());
        genericProduct.setRequiresPrescription(createGenericProductDto.isRequiresPrescription());
        return genericProduct;
    }
}
