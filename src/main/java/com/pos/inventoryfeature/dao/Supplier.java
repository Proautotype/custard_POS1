package com.pos.inventoryfeature.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String phoneNumber;
    private String email;

    @OneToMany(mappedBy = "supplier")
    private List<Purchase> purchaseList;
}
