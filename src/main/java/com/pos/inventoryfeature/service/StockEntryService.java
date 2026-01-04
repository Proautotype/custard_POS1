package com.pos.inventoryfeature.service;

import com.pos.inventoryfeature.StockEntryDto;
import com.pos.retailfeature.dao.product.ProductsRepository;
import com.pos.retailfeature.dao.stock.StockEntry;
import com.pos.retailfeature.dao.stock.StockEntryRepository;
import com.pos.shared.ProductMapper;
import com.pos.shared.StockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockEntryService {
    private final StockEntryRepository stockEntryRepository;
    private final ProductsRepository productsRepository;
    private final ProductMapper productMapper;
    private final StockMapper stockMapper;
    public void createStock(StockEntryDto stockEntryDto){
        StockEntry entity = stockMapper.toEntity(stockEntryDto);
        try {
            stockEntryRepository.save(entity);
            log.info("stock created successfully {} ", entity.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
