package com.pos.retailfeature.service;

import com.pos.retailfeature.dao.product.GenericProduct;
import com.pos.retailfeature.dao.product.GenericProductRepository;
import com.pos.retailfeature.dao.product.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenericProductService {

    private final GenericProductRepository genericProductRepository;
    private final ProductsRepository productRepository;

    /**
     * Retrieves all Generic Product metadata.
     */
    public List<GenericProduct> getAllGenerics() {
        return genericProductRepository.findAll();
    }

    /**
     * Finds a specific generic and its associated brands.
     */
    public GenericProduct getGenericById(String id) {
        return genericProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Generic Product not found with ID: " + id));
    }

    /**
     * Creates a new Generic metadata entry (e.g., adding "Ibuprofen" to the system).
     */
    @Transactional
    public GenericProduct createGeneric(GenericProduct genericProduct) {
        // You could add logic here to check if the category matches your YAML config
        return genericProductRepository.save(genericProduct);
    }

    /**
     * Business Logic: Calculate total stock available for a generic drug
     * by summing up all stock from all its different brands.
     */
    public Integer getTotalStockForGeneric(String genericId) {
        getGenericById(genericId);
        // Validate existence if necessary, then get the sum directly from DB
        return productRepository.sumStockByGenericId(genericId);
    }

    public List<GenericProduct> searchGenerics(Pageable pageable) {
        return genericProductRepository
                .findAll(pageable).stream().toList();
    }

    public long countFiltered(String filter) {
        return genericProductRepository.count();
    }
}
