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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPharmaceutical() {
        return isPharmaceutical;
    }

    public void setPharmaceutical(boolean pharmaceutical) {
        isPharmaceutical = pharmaceutical;
    }

    public boolean isRequiresPrescription() {
        return requiresPrescription;
    }

    public void setRequiresPrescription(boolean requiresPrescription) {
        this.requiresPrescription = requiresPrescription;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Product> getBrands() {
        return brands;
    }

    public void setBrands(List<Product> brands) {
        this.brands = brands;
    }
}
