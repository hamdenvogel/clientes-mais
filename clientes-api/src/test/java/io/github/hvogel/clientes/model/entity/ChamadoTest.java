package io.github.hvogel.clientes.model.entity;

import io.github.hvogel.clientes.enums.StatusChamado;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ChamadoTest {

    @Test
    void testGettersAndSetters() {
        Chamado chamado = new Chamado();
        Cliente cliente = new Cliente();
        
        chamado.setId(1L);
        chamado.setDescricao("Problema de rede");
        chamado.setCliente(cliente);
        chamado.setData(LocalDate.of(2025, 12, 13));
        chamado.setLocalAcontecimento("Escritório Principal");
        chamado.setStatus(StatusChamado.A);
        
        assertEquals(1L, chamado.getId());
        assertEquals("Problema de rede", chamado.getDescricao());
        assertEquals(cliente, chamado.getCliente());
        assertEquals(LocalDate.of(2025, 12, 13), chamado.getData());
        assertEquals("Escritório Principal", chamado.getLocalAcontecimento());
        assertEquals(StatusChamado.A, chamado.getStatus());
    }
    
    @Test
    void testPrePersist() {
        Chamado chamado = new Chamado();
        assertNull(chamado.getData());
        
        chamado.prePersist();
        
        assertNotNull(chamado.getData());
        assertEquals(LocalDate.now(), chamado.getData());
    }
    
    @Test
    void testEquals() {
        Chamado chamado1 = new Chamado();
        chamado1.setId(1L);
        chamado1.setDescricao("Teste");
        
        Chamado chamado2 = new Chamado();
        chamado2.setId(1L);
        chamado2.setDescricao("Teste");
        
        assertEquals(chamado1, chamado2);
    }
    
    @Test
    void testHashCode() {
        Chamado chamado1 = new Chamado();
        chamado1.setId(1L);
        
        Chamado chamado2 = new Chamado();
        chamado2.setId(1L);
        
        assertEquals(chamado1.hashCode(), chamado2.hashCode());
    }
    
    @Test
    void testToString() {
        Chamado chamado = new Chamado();
        chamado.setId(1L);
        chamado.setDescricao("Teste");
        
        String result = chamado.toString();
        assertNotNull(result);
    }
}
