package com.ajudaqui.controle.de.pagamentos30.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;

public interface BoletoRepository extends JpaRepository<Boleto, Long>,JpaSpecificationExecutor<Boleto> {

	List<Boleto> findByDescricao(String descricao);

	@Query(value="SELECT * FROM boleto WHERE status='PAGO'", nativeQuery = true)
	List<Boleto> findBoletosPagos();
	
	@Query(value="SELECT * FROM boleto WHERE status='VENCIDO'", nativeQuery = true)
	List<Boleto> findBoletosVencidos();
	
	@Query("SELECT b FROM Boleto b WHERE b.vencimento >= :inicioMes AND b.vencimento <= :finalMes")
	List<Boleto> findBoletosVencimentoProximo(LocalDate inicioMes, LocalDate finalMes );
	
	
;
	

}
