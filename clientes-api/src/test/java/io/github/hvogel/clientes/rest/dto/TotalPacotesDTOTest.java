package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseTotalDTOTest;

class TotalPacotesDTOTest extends BaseTotalDTOTest<TotalPacotesDTO> {

        @Override
        protected TotalPacotesDTO createDTO(Long total) {
                return TotalPacotesDTO.builder().withTotalPacotes(total).build();
        }

        @Override
        protected Long getTotal(TotalPacotesDTO dto) {
                return dto.getTotalPacotes();
        }

        @Override
        protected void setTotal(TotalPacotesDTO dto, Long total) {
                dto.setTotalPacotes(total);
        }
}
