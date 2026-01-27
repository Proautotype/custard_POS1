package com.pos.base.events;

import com.pos.inventoryfeature.dto.SupplierDto;

public record SupplierCreatedEvent(SupplierDto supplier) {
}
