package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseTotalDTOTest;

class TotalUsuariosDTOTest extends BaseTotalDTOTest<TotalUsuariosDTO> {

        @Override
        protected TotalUsuariosDTO createDTO(Long total) {
                return TotalUsuariosDTO.builder().withTotalUsuarios(total).build();
        }

        @Override
        protected Long getTotal(TotalUsuariosDTO dto) {
                return dto.getTotalUsuarios();
        }

        @Override
        protected void setTotal(TotalUsuariosDTO dto, Long total) {
                dto.setTotalUsuarios(total);
        }
}
