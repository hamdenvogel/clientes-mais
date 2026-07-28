package io.github.hvogel.clientes.config;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.PostgreSQLDialect;

/**
 * Dialeto PostgreSQL alinhado com a versão 16.3 em uso.
 * Deixa o Hibernate operar com as capacidades nativas do PostgreSQL 16,
 * sem workarounds de versões antigas.
 */
public class CustomPostgreSQLDialect extends PostgreSQLDialect {

    public CustomPostgreSQLDialect() {
        super(DatabaseVersion.make(16, 3));
    }
}
