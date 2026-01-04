package com.pos.core.extension;

import com.pos.core.domain.models.Sale;
import com.pos.core.domain.models.SaleItem;

/**
 * Contract for adding specialized, domain-specific logic to the generic POS transaction flow.
 * Implementations are loaded based on Spring Profiles (@Profile).
 */
public interface PosExtensionHook {

    /**
     * Executes specialized validation logic before an item is finalized.
     * @param sale The current sale being processed.
     * @param item The item being added/processed.
     * @throws SaleValidationException if a specialized rule is violated (e.g., Drug Interaction).
     */
    void preFinalizeItemCheck(Sale sale, SaleItem item) throws Exception;

    /**
     * Executes specialized logic after the entire sale is complete.
     * */
    void postFinalizeSale(Sale sale);
}
