package com.ajudaqui.controle.de.pagamentos30.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;

public interface BoletoRepository extends JpaRepository<Boleto, Long>,JpaSpecificationExecutor<Boleto> {

	List<Boleto> findByDescricao(String descricao);

	@Query(value="SELECT * FROM boleto WHERE status='PAGO' and vencimento BETWEEN :inicio AND :fim ", nativeQuery = true)
	List<Boleto> findBoletosPagos(LocalDate inicio, LocalDate fim);
	
	@Query(value="SELECT * FROM boleto WHERE status='VENCIDO'", nativeQuery = true)
	List<Boleto> findBoletosVencidos();
	
	@Query("SELECT b FROM Boleto b WHERE b.vencimento >= :inicioMes AND b.vencimento <= :finalMes")
	List<Boleto> findBoletosDoMes(LocalDate inicioMes, LocalDate finalMes );
	
	@Query("SELECT b FROM Boleto b WHERE b.status !='PAGO' AND b.vencimento >= :inicioMes AND b.vencimento <= :finalMes")
	List<Boleto> findBoletosASeremPagosNoMes(LocalDate inicioMes, LocalDate finalMes );
	
	@Query(value="select * from boleto where status <> 'PAGO' AND vencimento > :deadline ", nativeQuery = true)
	List<Boleto> nextPayments(LocalDate deadline);
	
;
	

}
