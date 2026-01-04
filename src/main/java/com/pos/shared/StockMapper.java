package com.pos.shared;

import com.pos.inventoryfeature.StockEntryDto;
import com.pos.retailfeature.dao.stock.StockEntry;
import org.springframework.stereotype.Service;

@Service
public class StockMapper {
    public  StockEntry toEntity(StockEntryDto stockEntryDto){
        StockEntry stockEntry = new StockEntry();
        stockEntry.setQuantitySoldFromThisBatch(stockEntryDto.getQuantitySoldFromThisBatch());
        stockEntry.setQuantityReceived(stockEntryDto.getQuantityReceived());
        stockEntry.setTotalBatchCost(stockEntryDto.getTotalBatchCost());
        stockEntry.setUnitCostPrice(stockEntryDto.getUnitCostPrice());
        stockEntry.setBatchNumber(stockEntryDto.getBatchNumber());
        stockEntry.setExpiryDate(stockEntryDto.getExpiryDate());
        stockEntry.setArrivalDate(stockEntryDto.getArrivalDate());
        return stockEntry;
    }
}
