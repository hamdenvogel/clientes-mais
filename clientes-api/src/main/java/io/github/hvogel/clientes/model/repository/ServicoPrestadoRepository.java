package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.rest.dto.ServicoPrestadoProjectionDTO;

public interface ServicoPrestadoRepository extends JpaRepository<ServicoPrestado, Integer> {

	@Query(" select s from ServicoPrestado s join s.cliente c " +
			" where upper(c.nome) like upper (:nome) and MONTH(s.data) = :mes ")
	List<ServicoPrestado> findByNomeClienteAndMes(
			@Param("nome") String nome, @Param("mes") Integer mes);

	@Query(value = "select distinct s.descricao from meusservicos.servicoprestado s " +
			" where upper(s.descricao) like upper (:descricao) and " +
			" extract(month from s.data) = :mes", nativeQuery = true)
	Optional<String> existsByDescricaoAndMes(
			@Param("descricao") String descricao, @Param("mes") Integer mes);

	long count();

	List<ServicoPrestado> findByDescricaoContainsAllIgnoreCase(String descricao);

	Page<ServicoPrestado> findByDescricaoContainsAllIgnoreCase(String descricao, Pageable pageable);

	@Query(" select s from ServicoPrestado s join s.cliente c " +
			" where c.id = :id ")
	Page<ServicoPrestado> findByIdCliente(@Param("id") Integer id, Pageable pageable);

	@Query(" select s from ServicoPrestado s join s.cliente c " +
			" where upper(c.nome) like %:nome% ")
	Page<ServicoPrestado> findByNomeCliente(@Param("nome") String nome, Pageable pageable);

	@Query(" select s from ServicoPrestado s where not exists ( " +
			"       select i from ItemPacote i " +
			"       where i.servicoPrestado = s ) " +
			"    order by s.id ")
	Page<ServicoPrestado> obterServicosAindaNaoVinculados(Pageable pageable);

	List<ServicoPrestado> findByCliente(Cliente cliente);

	@Modifying
	@Query("delete from ServicoPrestado s where s.cliente.id =:clientId")
	void deleteByIdCliente(@Param("clientId") Integer clientId);

	@Modifying
	@Query("delete from ServicoPrestado s where s.prestador.id =:prestadorId")
	void deleteByPrestadorId(@Param("prestadorId") Integer prestadorId);

	@Query("select s from ServicoPrestado s join s.cliente c where c.id =:id")
	List<ServicoPrestado> findByIdCliente(@Param("id") Integer id);

	boolean existsByCliente(Cliente cliente);

	boolean existsByPrestadorId(Integer idPrestador);

	@Query(value = "select " +
			"distinct to_char(sp.data, 'MM-YYYY') as mesAno " +
			"from meusservicos.servicoprestado sp " +
			"where sp.status in ('E', 'F', 'C') " +
			"and sp.data is not null " +
			"and date_part('year', sp.data) = date_part('year', CURRENT_DATE) " +
			"group by to_char(sp.data, 'MM-YYYY') " +
			"order by to_char(sp.data, 'MM-YYYY')", nativeQuery = true)
	List<String> mesesGrafico();

	@Query(value = "select " +
			"count(case sp.status " +
			"when 'E' then 'Em Atendimento' " +
			"else null " +
			"end) as emAtendimento " +
			"from meusservicos.servicoprestado sp " +
			"where sp.status in ('E', 'F', 'C') " +
			"and sp.data is not null " +
			"and date_part('year', sp.data) = date_part('year', CURRENT_DATE) " +
			"group by to_char(sp.data, 'MM-YYYY') " +
			"order by to_char(sp.data, 'MM-YYYY')", nativeQuery = true)
	List<Integer> emAtendimento();

	@Query(value = "select " +
			"count(case sp.status " +
			"when 'F' then 'Finalizado' " +
			"else null " +
			"end) as finalizado " +
			"from meusservicos.servicoprestado sp " +
			"where sp.status in ('E', 'F', 'C') " +
			"and sp.data is not null " +
			"and date_part('year', sp.data) = date_part('year', CURRENT_DATE) " +
			"group by to_char(sp.data, 'MM-YYYY') " +
			"order by to_char(sp.data, 'MM-YYYY')", nativeQuery = true)
	List<Integer> finalizado();

	@Query(value = "select " +
			"count(case sp.status " +
			"when 'C' then 'Cancelado' " +
			"else null " +
			"end) as cancelado " +
			"from meusservicos.servicoprestado sp " +
			"where sp.status in ('E', 'F', 'C') " +
			"and sp.data is not null " +
			"and date_part('year', sp.data) = date_part('year', CURRENT_DATE) " +
			"group by to_char(sp.data, 'MM-YYYY') " +
			"order by to_char(sp.data, 'MM-YYYY')", nativeQuery = true)
	List<Integer> cancelado();

	@Query(value = "select distinct(case sp.status " +
			" when 'C' then 'Cancelado' " +
			" when 'F' then 'Finalizado' " +
			" when 'E' then 'Em Atendimento' " +
			" else sp.status " +
			" end) as descricao_status " +
			"from meusservicos.servicoprestado sp " +
			"where date_part('year', sp.data) = date_part('year', CURRENT_DATE) " +
			"and sp.data is not null " +
			"order by (case sp.status " +
			" when 'C' then 'Cancelado' " +
			" when 'F' then 'Finalizado' " +
			" when 'E' then 'Em Atendimento' " +
			" else sp.status " +
			" end)", nativeQuery = true)
	List<String> descricaoStatus();

	@Query(value = "select count(case sp.status " +
			" when 'C' then 'Cancelado' " +
			" when 'F' then 'Finalizado' " +
			" when 'E' then 'Em Atendimento' " +
			" else sp.status " +
			" end) as quantidade " +
			"from meusservicos.servicoprestado sp " +
			"where sp.data is not null " +
			"and date_part('year', sp.data) = date_part('year', CURRENT_DATE) " +
			"group by sp.status " +
			"order by sp.status", nativeQuery = true)
	List<Integer> quantidadeServicos();

	@Query(value = "select " +
			"distinct to_char(sp.data, 'MM-YYYY') as mesAno " +
			"from meusservicos.servicoprestado sp " +
			"where sp.tipo in ('U', 'P') " +
			"and sp.data is not null " +
			"and date_part('year', sp.data) = date_part('year', CURRENT_DATE) " +
			"group by to_char(sp.data, 'MM-YYYY') " +
			"order by to_char(sp.data, 'MM-YYYY')", nativeQuery = true)
	List<String> mesesGraficoTipoServico();

	@Query(value = "select " +
			"count(case sp.tipo " +
			"when 'U' then 'Unit?rio' " +
			"else null " +
			"end) as unitario " +
			"from meusservicos.servicoprestado sp " +
			"where sp.tipo in ('U', 'P') " +
			"and sp.data is not null " +
			"and date_part('year', sp.data) = date_part('year', CURRENT_DATE) " +
			"group by to_char(sp.data, 'MM-YYYY') " +
			"order by to_char(sp.data, 'MM-YYYY')", nativeQuery = true)
	List<Integer> tipoUnitario();

	@Query(value = "select " +
			"count(case sp.tipo " +
			"when 'P' then 'Pacote' " +
			"else null " +
			"end) as unitario " +
			"from meusservicos.servicoprestado sp " +
			"where sp.tipo in ('U', 'P') " +
			"and sp.data is not null " +
			"and date_part('year', sp.data) = date_part('year', CURRENT_DATE) " +
			"group by to_char(sp.data, 'MM-YYYY') " +
			"order by to_char(sp.data, 'MM-YYYY')", nativeQuery = true)
	List<Integer> tipoPacote();

	@Query(" select new io.github.hvogel.clientes.rest.dto.ServicoPrestadoProjectionDTO " +
			" ( s.descricao as descricao, c.id as id, c.nome as nome, c.pix as pix ) " +
			" from ServicoPrestado s join s.cliente c " +
			" order by c.nome ")
	List<ServicoPrestadoProjectionDTO> findAllServicoPrestadoProjectionDTO();
}
