package com.pos.shared;

import com.pos.inventoryfeature.StockEntryDto;
import com.pos.inventoryfeature.dao.stock.StockEntry;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StockMapper {
    public  StockEntry toEntity(StockEntryDto stockEntryDto){
        StockEntry stockEntry = new StockEntry();
        stockEntry.setQuantityReceived(stockEntryDto.getQuantityReceived());
        stockEntry.setTotalBatchCost(stockEntryDto.getCostPrice().multiply(BigDecimal.valueOf(stockEntryDto.getQuantityReceived())));
        stockEntry.setUnitCostPrice(stockEntryDto.getCostPrice());
        stockEntry.setBatchNumber(stockEntryDto.getBatchNumber());
        stockEntry.setExpiryDate(stockEntryDto.getExpiryDate());
        stockEntry.setArrivalDate(stockEntryDto.getArrivalDate());
        return stockEntry;
    }
}
