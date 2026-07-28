package io.github.hvogel.clientes.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface CommonRepository<E extends IBaseEntity> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E>, Serializable {


}

