package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseTotalDTOTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TotalClientesDTOTest extends BaseTotalDTOTest<TotalClientesDTO> {

        @Override
        protected TotalClientesDTO createDTO(Long total) {
                return TotalClientesDTO.builder().withTotalClientes(total).build();
        }

        @Override
        protected Long getTotal(TotalClientesDTO dto) {
                return dto.getTotalClientes();
        }

        @Override
        protected void setTotal(TotalClientesDTO dto, Long total) {
                dto.setTotalClientes(total);
        }



        @Test
        void testNegativeNumber() {
                TotalClientesDTO dto = TotalClientesDTO.builder()
                                .withTotalClientes(-1L)
                                .build();

                assertEquals(-1L, dto.getTotalClientes());
        }
}
