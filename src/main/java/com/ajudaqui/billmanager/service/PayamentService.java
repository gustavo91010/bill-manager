package com.ajudaqui.billmanager.service;

import static com.ajudaqui.billmanager.utils.StatusBoleto.valueOf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajudaqui.billmanager.controller.from.BoletoFrom;
import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.exception.NotFoundEntityException;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.service.vo.PayamentDto;
import com.ajudaqui.billmanager.utils.StatusBoleto;
import com.ajudaqui.billmanager.utils.ValidarStatus;

@Service
public class PayamentService {

	@Autowired
	private PaymentsRepository paymentRepository;
	@Autowired
	private UsersService usersService;

	public Payment cadastrar(PayamentDto paymentDto, Long userId) {
		Users users = usersService.findById(userId);

		List<Payment> paymentForMonth = findAllMonth(userId, paymentDto.getDue_date().getMonthValue(),
				paymentDto.getDue_date().getYear());

		boolean alrreadRegistered = paymentForMonth.stream()
				.anyMatch(p -> p.getDescription().equals(paymentDto.getDescription())
						&& p.getValue().equals(paymentDto.getValue())
						&& p.getDue_date().equals(paymentDto.getDue_date()));

		if (alrreadRegistered) {
			throw new MsgException("pagamento já cadastrado");
		}
		Payment payment = paymentDto.toDatabase(paymentRepository, users);

		payment = paymentRepository.save(payment);
		users.getPayaments().add(payment);

		return payment;

	}

	public void boletosRecorrentes(PayamentDto boletoDto, Long repeticao, Long userId) {

//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		int index = 0;
		do {
//			LocalDate vencimento = LocalDate.parse(boletoDto.getDue_date().toString(), formatter);

			if (index > 0) {
//				boletoDto.setDue_date(vencimento.plusMonths(1));
				boletoDto.setDue_date(boletoDto.getDue_date().plusMonths(1));

			}
			cadastrar(boletoDto, userId);
			index++;
		} while (index < repeticao);

	}

	public List<Payment> searchLatePayments(Long usersId) {
		return paymentRepository.searchLatePayments(usersId);

	}

	public Payment findByIdForUsers(Long usersId, Long paymentId) {

		Payment boleto = paymentRepository.findByIdForUsers(usersId, paymentId)
				.orElseThrow(() -> new RuntimeException("Boleto não encontrado."));
		return boleto;
	}

	public Payment findById(Long paymentId) {

		Payment boleto = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Boleto não encontrado."));
		return boleto;
	}

	public List<Payment> findByPayamentsForUser(Long userId) {

		return paymentRepository.findByPayamentsForUser(userId);
	}

	public List<Payment> findPaymentsWeek(Long usersId, String date, String status) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dayWeek = LocalDate.parse(date, formatter);
		LocalDate monday = dayWeek.minusDays(dayWeek.getDayOfWeek().getValue() - 1);
		LocalDate sunday = monday.plusDays(6);
		
		List<Payment> payments = new ArrayList<Payment>();

		if (status.isEmpty()) {
			payments = paymentRepository.findPayamentsInMonth(usersId, monday, sunday);
			
		} else {
			payments = paymentRepository.findForPaymentsByMonthAndStatus(usersId, monday, sunday,
					valueOf(status));
		}
		return payments;

	}

	public List<Payment> findAllMonth(Long userId, Integer month, Integer year) {

		if (!usersService.userExist(userId)) {
			throw new NotFoundEntityException("usuario não cadastrado");
		}
		LocalDate startMonth = LocalDate.of(year, month, 1);
		LocalDate endMonth = LocalDate.of(year, month, startMonth.lengthOfMonth());
		List<Payment> resultt = paymentRepository.findAllMonth(userId, startMonth, endMonth);
		return resultt;

	}

	public List<Payment> searcheByUsersByMonthAndStatus(Long usersId, Integer month, Integer year, String status) {

		// se o mes tiver vazio, sera o periodo de 12 meses, o ano, sera 1900
		LocalDate startMonth = LocalDate.of(year == 0 ? 1900 : year, month == 0 ? 1 : month, 1);
		LocalDate endMonth = LocalDate.of(year == 0 ? 1900 : year, month == 0 ? 12 : month, startMonth.lengthOfMonth());

		List<Payment> payments = new ArrayList<Payment>();

		if (status.isEmpty()) {
			payments = paymentRepository.findPayamentsInMonth(usersId, startMonth, endMonth);
		} else {

			payments = paymentRepository.findForPaymentsByMonthAndStatus(usersId, startMonth, endMonth,
					valueOf(status));
		}
		return payments;
	}

	public List<Payment> findByDescricao(Long usersId, String descricao) {

		List<Payment> boletos = paymentRepository.findByDescriptionForUsers(usersId, descricao);
		return boletos;

	}

	// Efetivar Pagamento
	public Payment makePayment(Long id) {
		Payment boleto = findById(id);

		boleto.setStatus(StatusBoleto.PAGO);
		boleto.setUpdated_at(LocalDateTime.now());

		paymentRepository.save(boleto);
		return boleto;

	}

	public Payment update(Long userId, Long paymentId, BoletoFrom from) {
		Payment payment = findByIdForUsers(userId, paymentId);

		if (!from.getDescricao().isEmpty()) {
			payment.setDescription(from.getDescricao());
		}
		if (from.getValor() != null) {
			payment.setValue(from.getValor());
		}
		if (from.getVencimento() != null) {
			payment.setDue_date(from.getVencimento());
		}
		payment.setUpdated_at(LocalDateTime.now());
		return paymentRepository.save(payment);
	}

	// atualização do estado em execução
	public void performStatusUpdate() {
		List<Payment> pagamentos = paymentRepository.nextPayments(LocalDate.now().plusDays(10));
		atualizarStatus(pagamentos);
	}

	public List<Payment> atualizarStatus(List<Payment> boletos) {
		boletos.forEach(b -> {
			ValidarStatus.statusAtualizado(b, paymentRepository);

		});
		return boletos;

	}

	public void deleteById(Long id) {
		Payment boleto = findById(id);
		paymentRepository.delete(boleto);

	}

}
