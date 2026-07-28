package io.github.hvogel.clientes.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateDialectConfig {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateDialectConfig.class);
    private static final String CUSTOM_DIALECT = CustomPostgreSQLDialect.class.getName();

    @Bean
    public HibernatePropertiesCustomizer forceCustomPostgreSql16Dialect() {
        return (hibernateProperties) -> {
            hibernateProperties.put(AvailableSettings.DIALECT, CUSTOM_DIALECT);
        };
    }

    @Bean
    public ApplicationRunner logEffectiveHibernateDialect(EntityManagerFactory entityManagerFactory) {
        return (args) -> {
            SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
            String effectiveDialect = sessionFactory.getJdbcServices().getDialect().getClass().getName();
            LOG.info("Effective Hibernate dialect: {}", effectiveDialect);
        };
    }
}

