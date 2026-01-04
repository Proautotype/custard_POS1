package com.pos.core.service;

import com.pos.core.domain.models.Sale;
import com.pos.core.domain.models.SaleItem;
import com.pos.core.domain.repositories.SaleRepository;
import com.pos.core.extension.PosExtensionHook;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class PosService {
    // Spring injects all beans implementing the hook interface
    private final List<PosExtensionHook> extensionHooks;
    private final SaleRepository saleRepository;

    public Sale startNewScale(){
        return new Sale();
    }

    public Sale findSale(UUID id){
        return saleRepository.findById(id).orElseThrow(()->new RuntimeException("Sale no found"));
    }

    /**
     * Handles adding an item, running all specialized pre-checks and saving
     * */
    public Sale addItemToSale(Sale sale, String barcode, int quantity) throws Exception {
        // --- 1. Generic lookup logic (placeholder)
        SaleItem newItem = lookupProduct(barcode, quantity);

        // --- 2. Extension hooks (pre-check) ---
        // This loop runs 0 hook (Grocery) or 1 hook (Pharmacy) depending on profile.
        for (PosExtensionHook hook: extensionHooks){
            hook.preFinalizeItemCheck(sale, newItem);
        }

        // --- 3. Generic POS Logic
        sale.addItem(newItem);
        return saleRepository.save(sale);
    }

    /**
     * Handles finalizing the sale, including post-checks
     * */
    public Sale finalizeSale(Sale sale){
        // --- 1. GENERIC PAYMENT/ACCOUNTING LOGIC ---
        // (E.g., process credit card payment, generate invoice number, etc.)

        // --- 2. EXTENSION HOOKS (Post-Check)
        for(PosExtensionHook hook : extensionHooks){
            hook.postFinalizeSale(sale);
        }
        // --- 3. FINAL SAVE ---
        return saleRepository.save(sale);
    }

    // --- Mock Data/Lookup (Replace with actual ProductService in real app) ---
    private SaleItem lookupProduct(String barcode, int quantity) {
        switch (barcode) {
            case "RX001":
                return new SaleItem(barcode, "LISINOPRIL 20MG (RX)", new BigDecimal("10.00"), quantity);
            case "OTC101":
                return new SaleItem(barcode, "BAND-AIDS (OTC)", new BigDecimal("4.50"), quantity);
            case "GROC100":
                return new SaleItem(barcode, "ORGANIC BANANAS", new BigDecimal("0.79"), quantity);
            default:
                throw new RuntimeException("Product not found: " + barcode);
        }
    }
}
