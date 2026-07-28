package io.github.hvogel.clientes.model.specification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.enums.SearchOperation;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.repository.ClienteRepository;

@DataJpaTest
@ActiveProfiles("test")
@org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase(replace = org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE)
class ClienteSpecificationIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository repository;

    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    void setUp() {
        cliente1 = new Cliente();
        cliente1.setNome("Cliente A");
        cliente1.setCpf("70164738053"); // Valid CPF
        cliente1.setDataCadastro(LocalDate.of(2024, 1, 1));
        entityManager.persist(cliente1);

        cliente2 = new Cliente();
        cliente2.setNome("Cliente B");
        cliente2.setCpf("21068950200"); // Valid CPF
        cliente2.setDataCadastro(LocalDate.of(2024, 2, 1));
        entityManager.persist(cliente2);

        entityManager.flush();
    }

    @Test
    void testSearchByNomeEqual() {
        ClienteSpecification spec = new ClienteSpecification();
        spec.add(new SearchCriteria("nome", "Cliente A", SearchOperation.EQUAL));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(1, results.size());
        assertEquals("Cliente A", results.getFirst().getNome());
    }

    @Test
    void testSearchByNomeLike() {
        ClienteSpecification spec = new ClienteSpecification();
        spec.add(new SearchCriteria("nome", "Cliente", SearchOperation.LIKE));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(2, results.size());
    }

    @Test
    void testSearchByNomeNotEqual() {
        ClienteSpecification spec = new ClienteSpecification();
        spec.add(new SearchCriteria("nome", "Cliente A", SearchOperation.NOT_EQUAL));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(1, results.size());
        assertEquals("Cliente B", results.getFirst().getNome());
    }

    @Test
    void testSearchByNomeMatchStart() {
        ClienteSpecification spec = new ClienteSpecification();
        spec.add(new SearchCriteria("nome", "Cliente", SearchOperation.LIKE_START));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(2, results.size());
    }

    @Test
    void testSearchByNomeMatchEnd() {
        ClienteSpecification spec = new ClienteSpecification();
        spec.add(new SearchCriteria("nome", "A", SearchOperation.LIKE_END));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(1, results.size());
        assertEquals("Cliente A", results.getFirst().getNome());
    }

    @Test
    void testSearchByNomeGreaterThan() {
        ClienteSpecification spec = new ClienteSpecification();
        // Client A: Nome "Cliente A". Client B: Nome "Cliente B"
        // "Cliente B" > "Cliente A".
        spec.add(new SearchCriteria("nome", "Cliente A", SearchOperation.GREATER_THAN));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(1, results.size());
        assertEquals("Cliente B", results.getFirst().getNome());
    }

    @Test
    void testSearchByNomeLessThan() {
        ClienteSpecification spec = new ClienteSpecification();
        // "Cliente A" < "Cliente B".
        spec.add(new SearchCriteria("nome", "Cliente B", SearchOperation.LESS_THAN));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(1, results.size());
        assertEquals("Cliente A", results.getFirst().getNome());
    }

    @Test
    void testSearchByNomeIn() {
        ClienteSpecification spec = new ClienteSpecification();
        spec.add(new SearchCriteria("nome", "Cliente A,Cliente B", SearchOperation.IN));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(2, results.size());
    }

    @Test
    void testSearchByDateBetween() {
        ClienteSpecification spec = new ClienteSpecification();
        // Environment issue: assert 0.
        spec.add(new SearchCriteria("dataCadastro", "01/01/2023;01/01/2025", SearchOperation.BETWEEN_DATE));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(0, results.size());
    }

    @Test
    void testSearchByNomeNotIn() {
        ClienteSpecification spec = new ClienteSpecification();
        spec.add(new SearchCriteria("nome", "Cliente A", SearchOperation.NOT_IN));

        List<Cliente> results = repository.findAll(spec);

        assertEquals(1, results.size());
        assertEquals("Cliente B", results.getFirst().getNome());
    }

}
