package com.pos.retailfeature.service;

import com.pos.inventoryfeature.dao.Purchase;
import com.pos.retailfeature.dao.product.GenericProductRepository;
import com.pos.retailfeature.dao.product.Product;
import com.pos.retailfeature.dao.product.ProductsRepository;
import com.pos.retailfeature.dto.ProductDto;
import com.pos.shared.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductsRepository repository;
    private final ProductMapper productMapper;
    private final GenericProductRepository genericProductRepository;

    public void createProduct(ProductDto productDto){
        Product newProduct = productMapper.toEntity(productDto);
        genericProductRepository.findById(productDto.getProductGenericId()).ifPresent(newProduct::setGenericProduct);
        repository.save(newProduct);
    }

    public Page<Product> search(
            String query,
            Pageable pageable) {

        return repository.searchProducts(query, pageable)
                .map(e -> e);
    }

    public long count(String searchTerm) {
        return repository.countSearch(searchTerm);
    }

    @Transactional
    public void updateProductSellingPrice(String productId, Double value) {
        repository.findById(productId).ifPresent(
                product -> product.setCurrentSellingPrice(BigDecimal.valueOf(value)));
    }

    public Optional<Product> findById(String id) {
        return repository.findById(id);
    }
}
