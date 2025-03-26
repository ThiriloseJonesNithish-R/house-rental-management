package com.rentalmanagement.house_rental;

import lombok.Data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LombokTest {
    @Test
    public void testLombokAnnotations() {
        TestClass test = new TestClass();
        test.setName("test");
        assertEquals("test", test.getName());
        System.out.println("Lombok is working correctly!");
    }

    @Data
    private static class TestClass {
        private String name;
    }
}
