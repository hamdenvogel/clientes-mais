package io.github.hvogel.clientes.filter;

import io.github.hvogel.clientes.util.HttpServletReqUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionFilterTest {

    @InjectMocks
    private TransactionFilter transactionFilter;

    @Mock
    private HttpServletReqUtil reqUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void testDoFilter() throws Exception {
        when(reqUtil.getRemoteAddress(any())).thenReturn("127.0.0.1");
        when(reqUtil.getRequestParams(any())).thenReturn("param=value");
        when(reqUtil.getPayLoad(any())).thenReturn("{}");

        transactionFilter.doFilter(request, response, filterChain);

        verify(reqUtil).getRemoteAddress(any());
        verify(reqUtil).getRequestParams(any());
        verify(reqUtil).getPayLoad(any());
        verify(filterChain).doFilter(any(), eq(response));
    }
}
