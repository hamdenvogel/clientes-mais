package io.github.hvogel.clientes.util;

/**
 * Classe de constantes para mensagens do sistema.
 * Centraliza strings literais para evitar duplicação de código.
 */
public final class Messages {
    
    private Messages() {
        // Utility class - não deve ser instanciada
    }

    // Mensagens informativas
    public static final String MSG_INFORMACAO = "Informação";

    // Mensagens de erro - Entidades não encontradas
    public static final String CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado.";
    public static final String CLIENTE_INEXISTENTE = "Cliente inexistente.";
    public static final String PRESTADOR_NAO_ENCONTRADO = "Prestador não encontrado.";
    public static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado.";
    public static final String PRODUTO_NAO_ENCONTRADO = "Produto não encontrado.";
    public static final String PACOTE_NAO_ENCONTRADO = "Pacote não encontrado.";
    public static final String PACOTE_INEXISTENTE = "Pacote Inexistente";
    public static final String SERVICO_NAO_ENCONTRADO = "Serviço não encontrado.";
    public static final String SERVICO_INEXISTENTE = "Serviço Inexistente.";
    public static final String IMAGEM_NAO_ENCONTRADA = "Imagem não encontrada.";
    public static final String ROLE_NOT_FOUND_ERROR = "Error: Role is not found.";

    // Mensagens de validação
    public static final String CAMPO_AVALIACAO_OBRIGATORIO = "O campo avaliação é obrigatório!";
    public static final String VALOR_INVALIDO = "valor inválido.";

    // Constantes de documentos
    public static final String SIMPLE_BOOKMARK = "SimpleBookmark";
    public static final String ADD_BOOKMARKS_FILE = "AddBookmarks.docx";

    // Constantes de parâmetros
    public static final String PARAM_DATA_INICIO = "DATA_INICIO";
    public static final String PARAM_DATA_FIM = "DATA_FIM";

    // Valores padrão
    public static final String DEFAULT_VALUE = "default";
}
