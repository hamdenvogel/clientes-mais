package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseTotalDTOTest;

class TotalProdutosDTOTest extends BaseTotalDTOTest<TotalProdutosDTO> {

        @Override
        protected TotalProdutosDTO createDTO(Long total) {
                return TotalProdutosDTO.builder().withTotalProdutos(total).build();
        }

        @Override
        protected Long getTotal(TotalProdutosDTO dto) {
                return dto.getTotalProdutos();
        }

        @Override
        protected void setTotal(TotalProdutosDTO dto, Long total) {
                dto.setTotalProdutos(total);
        }
}
