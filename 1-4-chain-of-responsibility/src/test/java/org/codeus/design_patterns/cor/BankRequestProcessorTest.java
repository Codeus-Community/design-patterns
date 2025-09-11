package org.codeus.design_patterns.cor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankRequestProcessorTest {

    private BankRequestProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new BankRequestProcessor();
    }

    @Test
    void shouldProcessTransferAndApplyCommission() {
        BankRequest request = new BankRequest("1", RequestType.TRANSFER, 1000, "user1");
        processor.process(request);
        assertEquals(1010, request.getAmount(), 0.001);
    }

    @Test
    void shouldProcessBillPaymentAndApplyCommission() {
        BankRequest request = new BankRequest("2", RequestType.BILL_PAYMENT, 2000, "user2");
        processor.process(request);
        assertEquals(2020, request.getAmount(), 0.001);
    }

    @Test
    void shouldProcessCreditApplicationWithoutCommissionOrLimits() {
        BankRequest request = new BankRequest("3", RequestType.CREDIT_APPLICATION, 150_000, "user3");
        processor.process(request);
        assertEquals(150_000, request.getAmount(), 0.001);
    }

    @Test
    void shouldRejectNullType() {
        BankRequest request = new BankRequest("5", null, 1000, "user5");
        assertThrows(IllegalArgumentException.class,
                () -> processor.process(request));
    }

    @Test
    void shouldRejectWhenTransferExceedsLimit() {
        BankRequest request = new BankRequest("6", RequestType.TRANSFER, 60_000, "user6");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> processor.process(request));
        assertTrue(ex.getMessage().contains("Daily limit exceeded"));
    }

    @Test
    void shouldRejectWhenBillPaymentExceedsLimit() {
        BankRequest request = new BankRequest("7", RequestType.BILL_PAYMENT, 100_000, "user7");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> processor.process(request));
        assertTrue(ex.getMessage().contains("Daily limit exceeded"));
    }

    @Test
    void shouldAllowCreditApplicationExceedingLimit() {
        BankRequest request = new BankRequest("8", RequestType.CREDIT_APPLICATION, 100_000, "user8");
        assertDoesNotThrow(() -> processor.process(request));
    }

    @Test
    void shouldRejectWhenAmountExceedsAmlThreshold() {
        BankRequest request = new BankRequest("9", RequestType.TRANSFER, 250_000, "user9");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> processor.process(request));
        assertTrue(ex.getMessage().contains("AML"));
    }

    @Test
    void shouldAcceptAmountExactlyAtLimit() {
        BankRequest request = new BankRequest("10", RequestType.TRANSFER, 50_000, "user10");
        assertDoesNotThrow(() -> processor.process(request));
    }

    @Test
    void shouldAcceptAmountExactlyAtAmlThreshold() {
        BankRequest request = new BankRequest("11", RequestType.BILL_PAYMENT, 200_000, "user11");
        assertDoesNotThrow(() -> processor.process(request));
    }
}
