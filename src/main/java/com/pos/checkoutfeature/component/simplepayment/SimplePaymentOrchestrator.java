package com.pos.checkoutfeature.component.simplepayment;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pos.base.app_util.AuthUtil;
import com.pos.core.service.CartState;
import com.pos.inventoryfeature.service.StockEntryService;
import com.pos.retailfeature.dao.product.ProductsRepository;
import com.pos.retailfeature.dao.sale.Sale;
import com.pos.retailfeature.dao.sale.SaleItem;
import com.pos.retailfeature.dao.sale.SaleRepository;
import com.pos.retailfeature.subcomponent.ReceiptItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

@Component
@VaadinSessionScope
public class SimplePaymentOrchestrator {
    private final Logger logger = LoggerFactory.getLogger(SimplePaymentOrchestrator.class);

    private CartState cartState;
    private StockEntryService stockEntryService;
    private ProductsRepository productsRepository;
    private SaleRepository saleRepository;

    private Double currentAmount;
    private PaymentMode currentMode = PaymentMode.CASH;
    private String paymentNote;
    private String mobileMoneyNumber;

    Sale sale;

    public SimplePaymentOrchestrator(
            CartState cartState,
            StockEntryService stockEntryService,
            ProductsRepository productsRepository,
            SaleRepository saleRepository) {
        this.cartState = cartState;
        this.stockEntryService = stockEntryService;
        this.productsRepository = productsRepository;
        this.saleRepository = saleRepository;
    }

    public void onModeChanged(PaymentMode mode) {
        this.currentMode = mode;
        logger.info("Payment mode changed to {}", mode);
    }

    public void onAmountChanged(Double amount) {
        this.currentAmount = amount;
        logger.info("User entered: {} ", amount);
    }

    public void onNoteChanged(String note) {
        logger.info("Payment note changed to {}", note);
        this.paymentNote = note;
    }

    public void onMobileNumberChanged(String mobileNumber) {
       this.mobileMoneyNumber = mobileNumber;
       logger.info("Mobile money number changed to {}", mobileNumber);
    }

    @Transactional
    public void process(Double amount, PaymentMode mode) {
        try {
            logger.info("Processing payment begun");

            // check if sale is recorded
            if (!isSaleRecorded()) {
                logger.error("sale could not be recorded");
                return;
            }

            // deduct from stock
            for (ReceiptItem item : cartState.getCart()) {
                int quantity = item.getQuantity();
                String currentProductId = item.getProductId();

                logger.info("quantity -> {} | currentProductId -> {} ", quantity, currentProductId);

                stockEntryService.deductStock(currentProductId, quantity, item.getUnitPrice(), "RECEIPT");
            }
            logger.info("completed stock deduction and audit trail");

            // process payment
            logger.info("Processing payment of {} via {}", amount, mode);

            // clear the cart
            cartState.clear();
            UI.getCurrent().getPage().setLocation("/sell");
        } catch (Exception e) {

            logger.error("error processing payment --> " + e.getLocalizedMessage());
            logger.error("error processing payment --> " + e.getLocalizedMessage(), e);

        }
    }

    @Transactional
    boolean isSaleRecorded() {
        try {
            // implement logic to check if sale is recorded

            // Mock logic
            if (cartState.getCart().isEmpty()) {
                logger.error("Cart is empty");
                return false;
            }

            // create sale form currentSaleId and cart items
            sale = new Sale();
            sale.setId(cartState.getCurrentSaleid());
            sale.setPaymentMethod(this.currentMode);
            sale.setTotalAmount(cartState.getTotal());
            sale.setAmountTendered(BigDecimal.valueOf(this.currentAmount));
            sale.setPaymentNote(this.paymentNote);
            sale.setMobileMoneyNumber(this.mobileMoneyNumber != null ? this.mobileMoneyNumber : "N/A");
            sale.setPerformedBy(AuthUtil.currentUsername().toUpperCase());

            List<SaleItem> items = cartState.getCart().stream().map(receiptItem -> {
                SaleItem saleItem = new SaleItem();
                saleItem.setSale(sale);
                saleItem.setProduct(productsRepository.findById(receiptItem.getProductId()).orElse(null));
                saleItem.setQuantity(receiptItem.getQuantity());
                saleItem.setPriceAtSale(receiptItem.getUnitPrice());
                return saleItem;
            }).toList();

            sale.setItems(items);
            logger.info("Sale recorded with id {}", sale.getId());
              Sale savedSale = saleRepository.save(sale);
            logger.info("Sale recorded with id {}", savedSale.getId());
            return true;
        } catch (Exception e) {
            logger.error("error checking sale record --> " + e.getLocalizedMessage());
            logger.error("error checking sale record --> " + e.getLocalizedMessage(), e);
            return false;
        }
    }

}
