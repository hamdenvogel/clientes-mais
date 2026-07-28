package io.github.hvogel.clientes.util;

import jakarta.servlet.ServletInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MyHttpServletRequestWrapperTest {

    @Test
    void testGetInputStreamMultipleTimes() throws IOException {
        String content = "Request Content";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(content.getBytes());

        MyHttpServletRequestWrapper wrapper = new MyHttpServletRequestWrapper(request);

        // First read
        ServletInputStream inputStream1 = wrapper.getInputStream();
        byte[] bytes1 = new byte[content.length()];
        inputStream1.read(bytes1);
        assertEquals(content, new String(bytes1));

        // Second read (should be cached)
        ServletInputStream inputStream2 = wrapper.getInputStream();
        byte[] bytes2 = new byte[content.length()];
        inputStream2.read(bytes2);
        assertEquals(content, new String(bytes2));
    }

    @Test
    void testGetReader() throws IOException {
        String content = "Request Content";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(content.getBytes());

        MyHttpServletRequestWrapper wrapper = new MyHttpServletRequestWrapper(request);

        BufferedReader reader = wrapper.getReader();
        assertEquals(content, reader.readLine());
    }
}
