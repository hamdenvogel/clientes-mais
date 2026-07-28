package io.github.hvogel.clientes.model.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    @Test
    void testGettersAndSetters() {
        Endereco endereco = new Endereco();
        
        endereco.setCep("12345-678");
        endereco.setLogradouro("Rua Test");
        endereco.setComplemento("Apto 101");
        endereco.setBairro("Centro");
        endereco.setLocalidade("São Paulo");
        endereco.setUf("SP");
        
        assertEquals("12345-678", endereco.getCep());
        assertEquals("Rua Test", endereco.getLogradouro());
        assertEquals("Apto 101", endereco.getComplemento());
        assertEquals("Centro", endereco.getBairro());
        assertEquals("São Paulo", endereco.getLocalidade());
        assertEquals("SP", endereco.getUf());
    }
    
    @Test
    void testEquals() {
        Endereco endereco1 = new Endereco();
        endereco1.setCep("12345-678");
        endereco1.setLogradouro("Rua Test");
        endereco1.setComplemento("Apto 101");
        endereco1.setBairro("Centro");
        endereco1.setLocalidade("São Paulo");
        endereco1.setUf("SP");
        
        Endereco endereco2 = new Endereco();
        endereco2.setCep("12345-678");
        endereco2.setLogradouro("Rua Test");
        endereco2.setComplemento("Apto 101");
        endereco2.setBairro("Centro");
        endereco2.setLocalidade("São Paulo");
        endereco2.setUf("SP");
        
        assertEquals(endereco1, endereco2);
    }
    
    @Test
    void testEqualsWithDifferentBairro() {
        Endereco endereco1 = new Endereco();
        endereco1.setCep("12345-678");
        endereco1.setLogradouro("Rua Test");
        endereco1.setComplemento("Apto 101");
        endereco1.setBairro("Centro");
        endereco1.setLocalidade("São Paulo");
        endereco1.setUf("SP");
        
        Endereco endereco2 = new Endereco();
        endereco2.setCep("12345-678");
        endereco2.setLogradouro("Rua Test");
        endereco2.setComplemento("Apto 101");
        endereco2.setBairro("Jardim");
        endereco2.setLocalidade("São Paulo");
        endereco2.setUf("SP");
        
        assertNotEquals(endereco1, endereco2);
    }
    
    @Test
    void testEqualsWithDifferentComplemento() {
        Endereco endereco1 = new Endereco();
        endereco1.setCep("12345-678");
        endereco1.setComplemento("Apto 101");
        endereco1.setLocalidade("São Paulo");
        
        Endereco endereco2 = new Endereco();
        endereco2.setCep("12345-678");
        endereco2.setComplemento("Apto 102");
        endereco2.setLocalidade("São Paulo");
        
        assertNotEquals(endereco1, endereco2);
    }
    
    @Test
    void testEqualsWithDifferentLocalidade() {
        Endereco endereco1 = new Endereco();
        endereco1.setCep("12345-678");
        endereco1.setLocalidade("São Paulo");
        
        Endereco endereco2 = new Endereco();
        endereco2.setCep("12345-678");
        endereco2.setLocalidade("Rio de Janeiro");
        
        assertNotEquals(endereco1, endereco2);
    }
    
    @Test
    void testEqualsWithDifferentLogradouro() {
        Endereco endereco1 = new Endereco();
        endereco1.setLogradouro("Rua A");
        endereco1.setCep("12345-678");
        
        Endereco endereco2 = new Endereco();
        endereco2.setLogradouro("Rua B");
        endereco2.setCep("12345-678");
        
        assertNotEquals(endereco1, endereco2);
    }
    
    @Test
    void testEqualsWithDifferentUf() {
        Endereco endereco1 = new Endereco();
        endereco1.setCep("12345-678");
        endereco1.setUf("SP");
        
        Endereco endereco2 = new Endereco();
        endereco2.setCep("12345-678");
        endereco2.setUf("RJ");
        
        assertNotEquals(endereco1, endereco2);
    }
    
    @Test
    void testHashCode() {
        Endereco endereco1 = new Endereco();
        endereco1.setCep("12345-678");
        
        Endereco endereco2 = new Endereco();
        endereco2.setCep("12345-678");
        
        assertEquals(endereco1.hashCode(), endereco2.hashCode());
    }
    
    @Test
    void testToString() {
        Endereco endereco = new Endereco();
        endereco.setCep("12345-678");
        endereco.setLogradouro("Rua Test");
        
        String result = endereco.toString();
        assertNotNull(result);
        assertTrue(result.contains("12345-678") || result.contains("Endereco"));
    }
}
