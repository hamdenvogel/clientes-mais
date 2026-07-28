package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseTotalDTOTest;

class TotalPrestadoresDTOTest extends BaseTotalDTOTest<TotalPrestadoresDTO> {

        @Override
        protected TotalPrestadoresDTO createDTO(Long total) {
                return TotalPrestadoresDTO.builder().withTotalPrestadores(total).build();
        }

        @Override
        protected Long getTotal(TotalPrestadoresDTO dto) {
                return dto.getTotalPrestadores();
        }

        @Override
        protected void setTotal(TotalPrestadoresDTO dto, Long total) {
                dto.setTotalPrestadores(total);
        }
}
