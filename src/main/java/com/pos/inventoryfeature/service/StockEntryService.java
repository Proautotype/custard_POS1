package com.pos.inventoryfeature.service;

import com.pos.base.app_util.AuthUtil;
import com.pos.base.exceptions.InsufficientStockException;
import com.pos.inventoryfeature.StockEntryDto;
import com.pos.inventoryfeature.dao.StockMovementRepository;
import com.pos.inventoryfeature.dao.stock.StockEntry;
import com.pos.inventoryfeature.dao.stock.StockEntryRepository;
import com.pos.inventoryfeature.dao.stock.StockMovement;
import com.pos.inventoryfeature.dao.stock.StockMovementType;
import com.pos.shared.StockMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockEntryService {
    private final StockEntryRepository stockEntryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final StockMapper stockMapper;

    public void createStock(StockEntryDto stockEntryDto) {
        StockEntry entity = stockMapper.toEntity(stockEntryDto);
        try {
            stockEntryRepository.save(entity);
            log.info("stock created successfully {} ", entity.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deductStock(String productId, int quantityToSell, BigDecimal unitPrice, String reference) {

        long availableQty = stockEntryRepository.calculateAvailableProductStockEntry(productId);

        if (availableQty < quantityToSell) {
            throw new InsufficientStockException("Not enough stock for product " + productId);
        }

        List<StockEntry> batches = stockEntryRepository.findAvailableBatchesForSale(productId, LocalDate.now());
        int remainingToSell = quantityToSell;
        String username = AuthUtil.currentUsername();
        for (StockEntry batch : batches) {

            if (remainingToSell == 0)
                break;

            int availableInBatch = batch.getRemainingInBatch();
            int deduct = Math.min(availableInBatch, remainingToSell);

            batch.setQuantitySoldFromThisBatch(
                    batch.getQuantitySoldFromThisBatch() + deduct);

            // AUDIT RECORD
            StockMovement movement = new StockMovement();
            movement.setProduct(batch.getProduct());
            movement.setStockEntry(batch);
            movement.setType(StockMovementType.SALE);
            movement.setQuantity(deduct);
            movement.setPerformedBy(username);
            movement.setPerformedAt(LocalDateTime.now());
            movement.setReference(reference);
            movement.setUnitPrice(unitPrice);

            stockMovementRepository.save(movement);

            remainingToSell -= deduct;

        }

        if (remainingToSell > 0) {
            throw new InsufficientStockException(
                    "Not enough stock for product " + productId);
        }

    }

    @Transactional
    public void adjustStock(
            Long batchId, int adjustmentQty, String reason, String reference) {
        StockEntry batch = stockEntryRepository.findBatchForAdjustment(batchId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Batch not found"));

        String username = AuthUtil.currentUsername();

        int remaining = batch.getRemainingInBatch();

        // 🔒 Guard rails
        if (adjustmentQty == 0) {
            return;
        }

        if (adjustmentQty < 0 && Math.abs(adjustmentQty) > remaining) {
            throw new IllegalStateException("Adjustment exceeds available stock");
        }

        // apply adjustment
        if (adjustmentQty < 0) {
            batch.setQuantitySoldFromThisBatch(
                    batch.getQuantitySoldFromThisBatch() + Math.abs(adjustmentQty));
        } else {
            batch.setQuantityReceived(
                    batch.getQuantityReceived() + adjustmentQty);
        }

        // 🧾 Audit record
        StockMovement movement = new StockMovement();
        movement.setProduct(batch.getProduct());
        movement.setStockEntry(batch);
        movement.setType(StockMovementType.ADJUSTMENT);
        movement.setQuantity(Math.abs(adjustmentQty));
        movement.setAdjustmentReason(reason);
        movement.setPerformedBy(username);
        movement.setPerformedAt(LocalDateTime.now());
        movement.setReference(reference);

        stockMovementRepository.save(movement);

    }

}
