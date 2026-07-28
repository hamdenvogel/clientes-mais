package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseTotalDTOTest;

class TotalItensPacotesDTOTest extends BaseTotalDTOTest<TotalItensPacotesDTO> {

        @Override
        protected TotalItensPacotesDTO createDTO(Long total) {
                return TotalItensPacotesDTO.builder().withTotalItensPacotes(total).build();
        }

        @Override
        protected Long getTotal(TotalItensPacotesDTO dto) {
                return dto.getTotalItensPacotes();
        }

        @Override
        protected void setTotal(TotalItensPacotesDTO dto, Long total) {
                dto.setTotalItensPacotes(total);
        }
}
