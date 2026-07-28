package io.github.hvogel.clientes.response.jwt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MessageResponseTest {

    @Test
    void testConstructor() {
        MessageResponse response = new MessageResponse("Success");
        
        assertNotNull(response);
        assertEquals("Success", response.getMessage());
    }

    @Test
    void testConstructorEmpty() {
        MessageResponse response = new MessageResponse("");
        
        assertNotNull(response);
        assertEquals("", response.getMessage());
    }

    @Test
    void testConstructorNull() {
        MessageResponse response = new MessageResponse(null);
        
        assertNotNull(response);
        assertNull(response.getMessage());
    }

    @Test
    void testGetMessage() {
        MessageResponse response = new MessageResponse("Test message");
        
        assertEquals("Test message", response.getMessage());
    }

    @Test
    void testSetMessage() {
        MessageResponse response = new MessageResponse("Original");
        response.setMessage("Modified");
        
        assertEquals("Modified", response.getMessage());
    }

    @Test
    void testSetMessageNull() {
        MessageResponse response = new MessageResponse("Original");
        response.setMessage(null);
        
        assertNull(response.getMessage());
    }

    @Test
    void testLongMessage() {
        String longMessage = "This is a very long message that should still work properly";
        MessageResponse response = new MessageResponse(longMessage);
        
        assertEquals(longMessage, response.getMessage());
    }

    @Test
    void testSpecialCharacters() {
        String specialMessage = "Message with special chars: @#$%^&*()";
        MessageResponse response = new MessageResponse(specialMessage);
        
        assertEquals(specialMessage, response.getMessage());
    }
}
