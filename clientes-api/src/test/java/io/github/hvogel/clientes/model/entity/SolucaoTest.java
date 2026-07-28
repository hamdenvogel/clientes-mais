package io.github.hvogel.clientes.model.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SolucaoTest {

    @Test
    void testGettersAndSetters() {
        Solucao solucao = new Solucao();
        ServicoPrestado servicoPrestado = new ServicoPrestado();
        
        solucao.setId(1L);
        solucao.setDescricao("Manutenção preventiva");
        solucao.setServicoPrestado(servicoPrestado);
        solucao.setValor(new BigDecimal("150.00"));
        solucao.setDesconto(new BigDecimal("10.00"));
        
        assertEquals(1L, solucao.getId());
        assertEquals("Manutenção preventiva", solucao.getDescricao());
        assertEquals(servicoPrestado, solucao.getServicoPrestado());
        assertEquals(new BigDecimal("150.00"), solucao.getValor());
        assertEquals(new BigDecimal("10.00"), solucao.getDesconto());
    }
    
    @Test
    void testEquals() {
        Solucao solucao1 = new Solucao();
        solucao1.setId(1L);
        solucao1.setDescricao("Teste");
        
        Solucao solucao2 = new Solucao();
        solucao2.setId(1L);
        solucao2.setDescricao("Teste");
        
        assertEquals(solucao1, solucao2);
    }
    
    @Test
    void testHashCode() {
        Solucao solucao1 = new Solucao();
        solucao1.setId(1L);
        solucao1.setDescricao("Teste");
        
        Solucao solucao2 = new Solucao();
        solucao2.setId(1L);
        solucao2.setDescricao("Teste");
        
        assertEquals(solucao1.hashCode(), solucao2.hashCode());
    }
    
    @Test
    void testToString() {
        Solucao solucao = new Solucao();
        solucao.setId(1L);
        solucao.setDescricao("Teste");
        
        String result = solucao.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("Teste"));
    }
}
