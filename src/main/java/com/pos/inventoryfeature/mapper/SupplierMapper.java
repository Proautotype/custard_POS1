package com.pos.inventoryfeature.mapper;

import com.pos.inventoryfeature.dao.Supplier;
import com.pos.inventoryfeature.dto.SupplierDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;


public class SupplierMapper {
    SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);

    // Entity to DTO
    public static SupplierDto toDto(Supplier supplier) {

        return new SupplierDto(
                supplier.getId().toString(), supplier.getName(), supplier.getPhoneNumber(), supplier.getEmail()
        );
    }
}
