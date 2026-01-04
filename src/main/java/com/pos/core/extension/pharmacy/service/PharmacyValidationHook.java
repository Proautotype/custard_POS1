package com.pos.core.extension.pharmacy.service;

import com.pos.core.domain.models.Sale;
import com.pos.core.domain.models.SaleItem;
import com.pos.core.extension.PosExtensionHook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("pharmacy-domain")
@Slf4j
public class PharmacyValidationHook implements PosExtensionHook {

    private final DrugDataService drugDataService;

    @Override
    public void preFinalizeItemCheck(Sale sale, SaleItem item) throws Exception {
        // 1. Mandatory Drug Interaction Check for all RX items
        if (item.isPrescription()){
            if(drugDataService.hasCriticalInteraction(sale, item)){
                // This exception stops the transaction in PosService.addItemToSale()
                throw new Exception(
                        "CRITICAL DRUG INTERACTION: " + item.getName() +
                                " interacts with existing medications. Pharmacist consultation required."
                );
            }
        }
        // 2. Patient Allergy Check (Mocked patient lookup implied)
        if (drugDataService.isAllergenic(item.getName())) {
            throw new Exception("PATIENT ALLERGY ALERT: " + item.getName() + " confirmed allergenic.");
        }

        // 3. Age/ID Check (Simplified example for demonstration)
        if (item.getName().contains("OTC") && item.getUnitPrice().compareTo(new java.math.BigDecimal("20.00")) > 0) {
            // Assume certain high-value OTCs require ID
            throw new Exception("MANDATORY ID CHECK: High-value OTC sale requires ID verification.");
        }

    }

    @Override
    public void postFinalizeSale(Sale sale) {
        // Placeholder: Log prescription sales to a state-run Prescription Drug Monitoring Program (PDMP) database.
        log.info("[PHARMACY HOOK] Finalized sale ID: {}. Logging all RX items to PDMP.", sale.getId());
    }
}
