package com.cafepos.printing;

import vendor.legacy.LegacyThermalPrinter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdapterPatternTests {

    static class TestLegacyPrinter {
        int lastLen = -1;

        public void legacyPrint(byte[] payload) {
            lastLen = payload.length;
        }
    }

    static class TestAdapter implements Printer {
        private final TestLegacyPrinter adaptee;

        TestAdapter(TestLegacyPrinter adaptee) { this.adaptee = adaptee; }

        @Override
        public void print(String receiptText) {
            byte[] escpos = receiptText.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            adaptee.legacyPrint(escpos);
        }
    }

    @Test
    void adapter_convertsTextToBytes() {
        var fake = new TestLegacyPrinter();
        Printer p = new TestAdapter(fake);
        p.print("ABC");
        assertTrue(fake.lastLen >= 3);
    }

    @Test
    void adapter_handlesEmptyString() {
        var fake = new TestLegacyPrinter();
        Printer p = new TestAdapter(fake);
        p.print("");
        assertEquals(0, fake.lastLen);
    }

    @Test
    void adapter_handlesMultilineReceipt() {
        var fake = new TestLegacyPrinter();
        Printer p = new TestAdapter(fake);
        String receipt = "Order (LAT+L) x2\nSubtotal: 7.80\nTax (10%): 0.78\nTotal: 8.58";
        p.print(receipt);
        assertTrue(fake.lastLen > 0);
    }

    @Test
    void adapter_convertsCorrectLength() {
        var fake = new TestLegacyPrinter();
        Printer p = new TestAdapter(fake);
        String text = "Test Receipt";
        p.print(text);
        assertEquals(text.length(), fake.lastLen);
    }

    @Test
    void legacyPrinterAdapter_implementsPrinterInterface() {
        LegacyThermalPrinter legacy = new LegacyThermalPrinter();
        LegacyPrinterAdapter adapter = new LegacyPrinterAdapter(legacy);
        assertTrue(adapter instanceof Printer);
    }

    @Test
    void legacyPrinterAdapter_printsWithoutError() {
        LegacyThermalPrinter legacy = new LegacyThermalPrinter();
        Printer printer = new LegacyPrinterAdapter(legacy);
        assertDoesNotThrow(() -> printer.print("Test receipt"));
    }
}
