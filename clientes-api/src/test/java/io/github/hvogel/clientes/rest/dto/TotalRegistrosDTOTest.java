package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.util.EqualsTestHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TotalRegistrosDTOTest {

        @Test
        void testBuilder() {
                TotalRegistrosDTO dto = TotalRegistrosDTO.builder()
                                .withTotal(500)
                                .build();

                assertNotNull(dto);
                assertEquals(500, dto.getTotal());
        }

        @Test
        void testBuilderZero() {
                TotalRegistrosDTO dto = TotalRegistrosDTO.builder()
                                .withTotal(0)
                                .build();

                assertNotNull(dto);
                assertEquals(0, dto.getTotal());
        }

        @Test
        void testSetters() {
                TotalRegistrosDTO dto = TotalRegistrosDTO.builder()
                                .withTotal(100)
                                .build();

                dto.setTotal(250);

                assertEquals(250, dto.getTotal());
        }

        @Test
        void testEquals() {
                TotalRegistrosDTO dto1 = TotalRegistrosDTO.builder()
                                .withTotal(500)
                                .build();

                TotalRegistrosDTO dto2 = TotalRegistrosDTO.builder()
                                .withTotal(500)
                                .build();

                TotalRegistrosDTO dto3 = TotalRegistrosDTO.builder()
                                .withTotal(250)
                                .build();

                // Standard edge cases
                EqualsTestHelper.assertEqualsEdgeCases(dto1);
                
                // Specific equality tests
                assertEquals(dto1, dto2);
                assertNotEquals(dto1, dto3);
        }

        @Test
        void testHashCode() {
                TotalRegistrosDTO dto1 = TotalRegistrosDTO.builder()
                                .withTotal(500)
                                .build();

                TotalRegistrosDTO dto2 = TotalRegistrosDTO.builder()
                                .withTotal(500)
                                .build();

                assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        void testToString() {
                TotalRegistrosDTO dto = TotalRegistrosDTO.builder()
                                .withTotal(500)
                                .build();

                String result = dto.toString();

                assertNotNull(result);
                assertTrue(result.contains("500"));
        }
}
