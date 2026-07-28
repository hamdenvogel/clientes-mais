package io.github.hvogel.clientes.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import io.github.hvogel.clientes.enums.EPerfil;
import io.github.hvogel.clientes.enums.StatusDocumento;
import io.github.hvogel.clientes.enums.StatusServico;

class EntitiesTest {

    @Test
    void testImagemGettersSetters() {
        Imagem img1 = new Imagem();
        img1.setId(1);
        img1.setFileName("test.png");
        img1.setFileType("image/png");
        img1.setSize(100L);
        img1.setUuid("uuid");
        img1.setSystemName("sysname");
        byte[] data = new byte[] { 1, 2, 3 };
        img1.setData(data);
        img1.setChaveId(123);
        img1.setOriginalFileName("original.png");

        Documento doc = new Documento();
        doc.setId(1);
        img1.setDocumento(doc);

        assertEquals(1, img1.getId());
        assertEquals("test.png", img1.getFileName());
        assertEquals("image/png", img1.getFileType());
        assertEquals(100L, img1.getSize());
        assertEquals("uuid", img1.getUuid());
        assertEquals("sysname", img1.getSystemName());
        assertEquals(123, img1.getChaveId());
        assertEquals(doc, img1.getDocumento());
        assertEquals("original.png", img1.getOriginalFileName());
        assertEquals(data, img1.getData());
    }

    @Test
    void testImagemEqualsHashCode() {
        byte[] data = new byte[] { 1, 2, 3 };
        Documento doc = new Documento();
        doc.setId(1);

        Imagem img1 = createBaseImagem(data, doc);
        Imagem img2 = createBaseImagem(data, doc);

        assertEquals(img2, img1);
        assertEquals(img1.hashCode(), img2.hashCode());

        // Equals branch coverage
        assertNotEquals(img1, null);
        assertNotEquals(img1, new Object());

        Imagem diff = new Imagem();
        diff.setId(1);
        diff.setChaveId(999);
        assertNotEquals(img1, diff);

        diff = new Imagem();
        diff.setId(1);
        diff.setData(new byte[] { 4, 5 });
        assertNotEquals(img1, diff);

        diff = new Imagem();
        diff.setId(1);
        diff.setDocumento(new Documento());
        assertNotEquals(img1, diff);

        diff = new Imagem();
        diff.setId(1);
        diff.setFileName("diff.png");
        assertNotEquals(img1, diff);

        diff = new Imagem();
        diff.setId(1);
        diff.setFileType("diff/type");
        assertNotEquals(img1, diff);

        diff = new Imagem();
        diff.setId(1);
        diff.setOriginalFileName("diff.png");
        assertNotEquals(img1, diff);

        diff = new Imagem();
        diff.setId(1);
        diff.setSize(999L);
        assertNotEquals(img1, diff);

        diff = new Imagem();
        diff.setId(1);
        diff.setSystemName("diff");
        assertNotEquals(img1, diff);

        diff = new Imagem();
        diff.setId(1);
        diff.setUuid("diff-uuid");
        assertNotEquals(img1, diff);

        diff = new Imagem();
        diff.setId(1);
        diff.setStatus(false);
        assertNotEquals(img1, diff);
    }

    @Test
    void testImagemMisc() throws java.io.IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        Imagem img1 = new Imagem();
        img1.setData(data);

        // Scale branch coverage
        assertEquals(data, img1.scale(0, 100));
        assertEquals(data, img1.scale(100, 0));

        // Imagem.build()
        Imagem build = Imagem.build();
        assertNotNull(build.getUuid());
        assertTrue(build.isStatus());

        assertNotNull(img1.toString());

        // buildImagem
        org.springframework.web.multipart.MultipartFile mockFile = mock(
                org.springframework.web.multipart.MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.getBytes()).thenReturn(new byte[] { 4, 5, 6 });

        io.github.hvogel.clientes.helpers.FileNameHelper helper = new io.github.hvogel.clientes.helpers.FileNameHelper();
        Imagem builtImg = Imagem.buildImagem(mockFile, helper);
        assertNotNull(builtImg);
        assertEquals("test.jpg", builtImg.getOriginalFileName());
        assertEquals(1024L, builtImg.getSize());

        // test equals super fail handled in existing tests but let's be sure
        Imagem imgWithId1 = new Imagem();
        imgWithId1.setId(1);
        Imagem imgWithId2 = new Imagem();
        imgWithId2.setId(2);
        assertNotEquals(imgWithId1, imgWithId2);

        // setFiles
        img1.setFiles(mockFile);
        assertEquals("image/jpeg", img1.getFileType());
        assertEquals(1024L, img1.getSize());
    }

    @Test
    void testImagemDefault() throws java.io.IOException {
        Imagem def = Imagem.defaultImagem();
        assertNotNull(def);
        assertNotNull(def.getData());

        Imagem scaledDef = Imagem.defaultImagem(50, 50);
        assertNotNull(scaledDef);
        assertNotNull(scaledDef.getData());
        assertTrue(scaledDef.getData().length > 0);
    }

    @Test
    void testProfissao() {
        Profissao p1 = new Profissao();
        p1.setId(1);
        p1.setDescricao("Dev");

        assertEquals(1, p1.getId());
        assertEquals("Dev", p1.getDescricao());

        Profissao p2 = new Profissao();
        p2.setId(1);
        p2.setDescricao("Dev");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
    }

    @Test
    void testItemPacote() {
        ItemPacote item = new ItemPacote();
        item.setId(1);

        ServicoPrestado servico = new ServicoPrestado();
        item.setServicoPrestado(servico);

        Pacote pacote = new Pacote();
        item.setPacote(pacote);

        assertEquals(1, item.getId());
        assertEquals(servico, item.getServicoPrestado());
        assertEquals(pacote, item.getPacote());

        ItemPacote item2 = new ItemPacote();
        item2.setId(1);
        item2.setServicoPrestado(servico);
        item2.setPacote(pacote);

        assertEquals(item, item2);
        assertEquals(item.hashCode(), item2.hashCode());
        assertNotNull(item.toString());
    }

    @Test
    void testDiagnostico() {
        Diagnostico d1 = new Diagnostico();
        d1.setId(1L);
        d1.setDescricao("Diag");

        assertEquals(1L, d1.getId());
        assertEquals("Diag", d1.getDescricao());

        Diagnostico d2 = new Diagnostico();
        d2.setId(1L);
        d2.setDescricao("Diag");

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
        assertNotNull(d1.toString());
    }

    @Test
    void testNatureza() {
        Natureza n1 = new Natureza();
        n1.setId(1L);
        n1.setDescricao("Nat");

        assertEquals(1L, n1.getId());
        assertEquals("Nat", n1.getDescricao());

        Natureza n2 = new Natureza();
        n2.setId(1L);
        n2.setDescricao("Nat");

        assertEquals(n1, n2);
        assertNotNull(n1.toString());
    }

    @Test
    void testAtividade() {
        Atividade a1 = new Atividade();
        a1.setId(1L);
        a1.setDescricao("Ativ");

        assertEquals(1L, a1.getId());
        assertEquals("Ativ", a1.getDescricao());

        Atividade a2 = new Atividade();
        a2.setId(1L);
        a2.setDescricao("Ativ");

        assertEquals(a1, a2);
        assertNotNull(a1.toString());
    }

    @Test
    void testServicoPrestado() {
        ServicoPrestado s1 = new ServicoPrestado();
        s1.setId(1);
        s1.setStatus(StatusServico.E);

        assertEquals(StatusServico.E, s1.getStatus());
        assertNotNull(s1.toString());

        ServicoPrestado s2 = new ServicoPrestado();
        s2.setId(1);
        s2.setStatus(StatusServico.E);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testUsuario() {
        Usuario u1 = Usuario.builder()
                .id(1L)
                .username("user")
                .email("user@mail.com")
                .password("pass")
                .build();
        u1.setAtivo(true);

        Set<Perfil> roles = new HashSet<>();
        roles.add(new Perfil(EPerfil.ROLE_USER));
        u1.setRoles(roles);

        assertEquals(1L, u1.getId());
        assertEquals("user", u1.getUsername());
        assertEquals("user@mail.com", u1.getEmail());
        assertEquals("pass", u1.getPassword());
        assertEquals(roles, u1.getRoles());

        Usuario u2 = new Usuario("user", "user@mail.com", "pass");
        u2.setId(1L);
        u2.setAtivo(true);
        u2.setRoles(roles);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotNull(u1.toString());

        Usuario u3 = new Usuario();
        assertNotNull(u3);
    }

    @Test
    void testEquipamento() {
        Equipamento e1 = new Equipamento();
        e1.setId(1L);
        e1.setDescricao("Eqp");
        e1.setMarca("Marca");
        e1.setModelo("Mod");
        e1.setAnoFabricacao(2020);
        e1.setAnoModelo(2021);

        ServicoPrestado sp = new ServicoPrestado();
        e1.setServicoPrestado(sp);

        assertEquals(1L, e1.getId());
        assertEquals("Eqp", e1.getDescricao());
        assertEquals("Marca", e1.getMarca());
        assertEquals("Mod", e1.getModelo());
        assertEquals(2020, e1.getAnoFabricacao());
        assertEquals(2021, e1.getAnoModelo());
        assertEquals(sp, e1.getServicoPrestado());
        assertNotNull(e1.toString());
    }

    @Test
    void testDocumento() {
        Documento d1 = new Documento();
        d1.setId(1);
        d1.setDescricao("Doc");
        d1.setStatus(StatusDocumento.A);

        assertEquals(1, d1.getId());
        assertEquals("Doc", d1.getDescricao());
        assertEquals(StatusDocumento.A, d1.getStatus());

        Documento d2 = new Documento();
        d2.setId(1);
        d2.setDescricao("Doc");
        d2.setStatus(StatusDocumento.A);

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
        assertNotNull(d1.toString());

        // Test not equals
        Documento d3 = new Documento();
        d3.setId(2);
        assertNotEquals(d1, d3);
        assertNotEquals(null, d1);
        assertNotEquals(new Object(), d1);
    }

    @Test
    void testPerfil() {
        Perfil p1 = new Perfil();
        p1.setId(1);
        p1.setNome(EPerfil.ROLE_ADMIN);

        assertEquals(1, p1.getId());
        assertEquals(EPerfil.ROLE_ADMIN, p1.getNome());

        Perfil p2 = new Perfil(EPerfil.ROLE_ADMIN);
        p2.setId(1);

        // Perfil doesn't have equals/hashCode overrides in the provided code, so we
        // test fields
        assertEquals(p1.getId(), p2.getId());
        assertEquals(p1.getNome(), p2.getNome());
    }

    @Test
    void testCaptcha() {
        Captcha c1 = new Captcha();
        c1.setSite("site-key");
        c1.setSecret("secret-key");

        assertEquals("site-key", c1.getSite());
        assertEquals("secret-key", c1.getSecret());

        Captcha c2 = new Captcha();
        c2.setSite("site-key");
        c2.setSecret("secret-key");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotNull(c1.toString());

        Captcha c3 = new Captcha();
        c3.setSite("other");
        assertNotEquals(c1, c3);
    }

    @Test
    void testCliente() {
        Cliente c1 = createBaseCliente();
        Cliente c2 = createBaseCliente();

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotNull(c1.toString());

        // Equals branches
        assertNotEquals(null, c1);
        assertNotEquals(c1, new Object());

        Cliente diff = new Cliente();
        diff.setId(1);
        diff.setNome("Diff");
        assertNotEquals(c1, diff);

        diff = new Cliente();
        diff.setId(1);
        diff.setCpf("00000000000");
        assertNotEquals(c1, diff);

        diff = new Cliente();
        diff.setId(1);
        diff.setCep("00000000");
        assertNotEquals(c1, diff);

        diff = new Cliente();
        diff.setId(1);
        diff.setDataCadastro(java.time.LocalDate.now().plusDays(1));
        assertNotEquals(c1, diff);
    }

    @Test
    void testPrestador() {
        Prestador p1 = createBasePrestador();
        Prestador p2 = createBasePrestador();

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());

        // Equals branches
        assertNotEquals(null, p1);
        assertNotEquals(p1, new Object());

        Prestador diff = new Prestador();
        diff.setId(1);
        diff.setNome("Diff");
        assertEquals(p1, diff); // Mesmo ID = objetos iguais

        diff = new Prestador();
        diff.setId(1);
        diff.setCpf("00000000000");
        assertEquals(p1, diff); // Mesmo ID = objetos iguais

        diff = new Prestador();
        diff.setId(1);
        diff.setEmail("diff@mail.com");
        assertEquals(p1, diff); // Mesmo ID = objetos iguais

        diff = new Prestador();
        diff.setId(1);
        diff.setAvaliacao(1);
        assertEquals(p1, diff); // Mesmo ID = objetos iguais
    }

    @Test
    void testPacote() {
        Pacote p1 = new Pacote();
        p1.setId(1);
        p1.setDescricao("Pacote 1");
        p1.setJustificativa("Justificativa 1");
        p1.setStatus("P");
        p1.setData(java.time.LocalDate.now());

        assertEquals(1, p1.getId());
        assertEquals("Pacote 1", p1.getDescricao());
        assertEquals("Justificativa 1", p1.getJustificativa());
        assertEquals("P", p1.getStatus());
        assertNotNull(p1.getData());

        Pacote p2 = new Pacote();
        p2.setId(1);
        p2.setDescricao("Pacote 1");
        p2.setJustificativa("Justificativa 1");
        p2.setStatus("P");
        p2.setData(p1.getData());

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
    }

    @Test
    void testEndereco() {
        Endereco e1 = new Endereco();
        e1.setLogradouro("Rua 1");
        e1.setBairro("Bairro 1");
        e1.setLocalidade("Cidade 1");
        e1.setUf("UF");
        e1.setCep("12345678");

        assertEquals("Rua 1", e1.getLogradouro());
        assertEquals("Bairro 1", e1.getBairro());
        assertEquals("Cidade 1", e1.getLocalidade());
        assertEquals("UF", e1.getUf());
        assertEquals("12345678", e1.getCep());

        Endereco e2 = new Endereco();
        e2.setLogradouro("Rua 1");
        e2.setBairro("Bairro 1");
        e2.setLocalidade("Cidade 1");
        e2.setUf("UF");
        e2.setCep("12345678");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotNull(e1.toString());
    }

    private Imagem createBaseImagem(byte[] data, Documento doc) {
        Imagem img = new Imagem();
        img.setId(1);
        img.setFileName("test.png");
        img.setFileType("image/png");
        img.setSize(100L);
        img.setUuid("uuid");
        img.setSystemName("sysname");
        img.setData(data);
        img.setChaveId(123);
        img.setOriginalFileName("original.png");
        img.setDocumento(doc);
        img.setStatus(true);
        return img;
    }

    private Cliente createBaseCliente() {
        Cliente c = new Cliente();
        c.setId(1);
        c.setNome("Cliente 1");
        c.setCpf("12345678901");
        c.setDataCadastro(java.time.LocalDate.now());
        return c;
    }

    private Prestador createBasePrestador() {
        Prestador p = new Prestador();
        p.setId(1);
        p.setNome("Prestador 1");
        p.setCpf("12345678901");
        p.setEmail("p1@mail.com");
        p.setPix("pix");
        p.setAvaliacao(5);
        p.setDataCadastro(java.time.LocalDate.now());
        return p;
    }
}
