package com.pos.checkoutfeature.component.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultPaymentOrchestrator implements PaymentOrchestrator {

    private Logger logger = LoggerFactory.getLogger(DefaultPaymentOrchestrator.class.getName());

    @Override
    public void handle(PaymentEvent event) {
        PaymentSession session = event.session();

        if (!event.confirmed()) {
            logger.info("Preview: " + session.balance());
            return;
        }

        switch (session.option()) {
            case CARD -> handleCard(session);
            case CASH -> handleCash(session);
            case MOBILE_MONEY -> handleMobile(session);
        }

    }

    private void handleCash(PaymentSession session) {
        if (!session.isValid()) {
            throw new IllegalStateException("Invalid cash amount");
        }

        // double change = session.enteredAmount() - session.payable();

        // session.markPaid();

        // receiptService.printCashReceipt(session, change);
    }

    private void handleCard(PaymentSession session) {
        session.markPending();

        // CardChargeRequest request = CardChargeRequest.from(session);

        // CardChargeResult result = cardGateway.charge(request);

        // if (result.success()) {
        // session.markPaid();
        // } else {
        // session.markFailed(result.reason());
        // }
    }

    private void handleMobile(PaymentSession session) {
        // session.markPending();

        // MobilePaymentRequest request = MobilePaymentRequest.from(session);

        // mobileGateway.initiate(request);

        // STOP HERE — callback will finish the job
    }

}
