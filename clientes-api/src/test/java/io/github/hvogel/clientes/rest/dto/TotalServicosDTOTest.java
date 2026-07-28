package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseTotalDTOTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TotalServicosDTOTest extends BaseTotalDTOTest<TotalServicosDTO> {

        @Override
        protected TotalServicosDTO createDTO(Long total) {
                return TotalServicosDTO.builder().withTotalServicos(total).build();
        }

        @Override
        protected Long getTotal(TotalServicosDTO dto) {
                return dto.getTotalServicos();
        }

        @Override
        protected void setTotal(TotalServicosDTO dto, Long total) {
                dto.setTotalServicos(total);
        }

        @Test
        void testLargeNumber() {
                TotalServicosDTO dto = TotalServicosDTO.builder()
                                .withTotalServicos(1000000L)
                                .build();

                assertEquals(1000000L, dto.getTotalServicos());
        }
}
