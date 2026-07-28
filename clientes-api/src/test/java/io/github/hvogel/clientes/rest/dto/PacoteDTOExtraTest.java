package io.github.hvogel.clientes.rest.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PacoteDTOExtraTest {

    @Test
    void testPrePersist() {
        PacoteDTO dto = new PacoteDTO();
        assertNull(dto.getData());

        dto.prePersist();

        assertNotNull(dto.getData());
        assertEquals(LocalDate.now(), dto.getData());
    }
}
