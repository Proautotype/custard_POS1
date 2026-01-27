package com.pos.retailfeature.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class CreateGenericProductDto implements Serializable {
    private String id;
    private String name;
    private final boolean isPharmaceutical = false;
    private final boolean requiresPrescription = false;
    private String warning;
    private String category;
}
