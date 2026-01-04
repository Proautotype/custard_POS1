package com.pos.retailfeature.dao.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "generic_products")
@Getter
@Setter
public class GenericProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;           // e.g., "Aspirin"
    private boolean isPharmaceutical = false;
    private boolean requiresPrescription = false;
    private String warning;
    @Column(nullable = false)
    private String category;
    @OneToMany(mappedBy = "genericProduct")
    private List<Product> brands; // List of all brands available for this generic
}
