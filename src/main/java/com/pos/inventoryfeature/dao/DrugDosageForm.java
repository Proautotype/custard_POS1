package com.pos.inventoryfeature.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(
        name = "drug_dosage_form",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_drug_dosage_form_code",
                        columnNames = "code"
                )
        }
)
@Getter
@Setter
public class DrugDosageForm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    protected DrugDosageForm() {
        // JPA only
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrugDosageForm)) return false;
        DrugDosageForm that = (DrugDosageForm) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}