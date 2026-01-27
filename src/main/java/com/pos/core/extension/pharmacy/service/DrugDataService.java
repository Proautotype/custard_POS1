package com.pos.core.extension.pharmacy.service;

import com.pos.core.domain.models.Sale;
import com.pos.core.domain.models.SaleItem;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("pharmacy-domain") // Only load this service in the pharmacy environment
public class DrugDataService {
    /**
     * Checks if the item being added interacts critically with any existing item
     * in the current sale or the patient's history.
     */
    public boolean hasCriticalInteraction(Sale sale, SaleItem newItem) {
        // Mock Logic: Check if LISINOPRIL is already present.
        if (newItem.getName().contains("LISINOPRIL") || sale.getItems().stream().anyMatch(i -> i.getName().contains("LISINOPRIL"))) {
            // In a real app, this would query a dedicated drug database.
            return sale.getItems().stream().anyMatch(i -> i.getName().contains("IBUPROFEN")) || newItem.getName().contains("IBUPROFEN");
        }
        return false;
    }

    /**
     * Checks a mock patient profile for allergies relevant to the new item.
     */
    public boolean isAllergenic(String itemName) {
        // Mock Logic: Always return true for Penicillin if it were an item.
        return itemName.toUpperCase().contains("PENICILLIN");
    }
}
