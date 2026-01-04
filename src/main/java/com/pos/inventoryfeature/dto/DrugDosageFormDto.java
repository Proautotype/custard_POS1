package com.pos.inventoryfeature.dto;

import com.pos.inventoryfeature.dao.DrugDosageForm;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class DrugDosageFormDto implements Serializable {

    private UUID id;
    private String code;
    private String name;

    public DrugDosageFormDto() {
    }

    public DrugDosageFormDto(
            UUID id,
            String code,
            String name
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    /* -------------------- factory -------------------- */

    public static DrugDosageFormDto fromEntity(DrugDosageForm entity) {
        return new DrugDosageFormDto(
                entity.getId(),
                entity.getCode(),
                entity.getName()
        );
    }

    /* -------------------- toString -------------------- */

    @Override
    public String toString() {
        return "DrugDosageFormDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
