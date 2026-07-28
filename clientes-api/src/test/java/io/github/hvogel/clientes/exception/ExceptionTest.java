package io.github.hvogel.clientes.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void testFileNameException() {
        FileNameException ex = new FileNameException("message");
        assertEquals("message", ex.getMessage());

        RuntimeException cause = new RuntimeException("cause");
        FileNameException exCause = new FileNameException("message", cause);
        assertEquals("cause", exCause.getCause().getMessage());
    }

    @Test
    void testPedidoNaoEncontradoException() {
        PedidoNaoEncontradoException ex = new PedidoNaoEncontradoException();
        assertEquals("Pedido não encontrado.", ex.getMessage());
    }

    @Test
    void testRegraNegocioException() {
        RegraNegocioException ex = new RegraNegocioException("msg");
        assertEquals("msg", ex.getMessage());
    }

    @Test
    void testSenhaInvalidaException() {
        SenhaInvalidaException ex = new SenhaInvalidaException();
        assertEquals("Senha Inválida", ex.getMessage());
    }

    @Test
    void testUsuarioCadastradoException() {
        UsuarioCadastradoException ex = new UsuarioCadastradoException("msg");
        assertEquals("Usuário já cadastrado para o login msg", ex.getMessage());
    }
}
