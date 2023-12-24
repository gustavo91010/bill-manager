package com.ajudaqui.billmanager.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

//	@Autowired
//	private ModelMapper modelMapper;
//
//	@Autowired
//	private EmailClient emailClient;

	public Payment cadastrar(PayamentDto paymentDto, Long userId) {
		Users users = usersService.findById(userId);

		List<Payment> paymentForMonth = findAllMonth(userId, paymentDto.getDue_date().getMonthValue(),
				paymentDto.getDue_date().getYear());

		boolean alrreadRegistered = paymentForMonth.stream()
				.anyMatch(p -> p.getDescription().equals(paymentDto.getDescription())
						&& p.getValue().equals(paymentDto.getValue()));

		if (alrreadRegistered) {
			throw new MsgException("pagamento já cadastrado");
		}
		Payment payment = paymentRepository.save(paymentDto.toDatabase(paymentRepository, users));
		return payment;

	}

	public void boletosRecorrentes(PayamentDto boletoDto, Long repeticao, Long userId) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

//		for (int i = 0; i < repeticao; i++) {
//
//			LocalDate vencimento = LocalDate.parse(boletoDto.getDue_date().toString(), formatter);
//			boletoDto.setDue_date(vencimento.plusMonths(1));
//			System.err.println(boletoDto.toString());
//			cadastrar(boletoDto,userId);
//		}
		int index = 0;
		do {
			LocalDate vencimento = LocalDate.parse(boletoDto.getDue_date().toString(), formatter);
			if (index > 0) {
				boletoDto.setDue_date(vencimento.plusMonths(1));
			}
			System.err.println(boletoDto.toString());
			cadastrar(boletoDto, userId);
			index++;
		} while (index < repeticao);

	}

//// não lembro bem o que esse cara faz...
//	public List<PayamentVO> findAll(BoletoFrom boletoFrom) {
//		Specification<Payment> spefications = Specification
//				.where(BoletoSpecification.descricao(boletoFrom.getDescricao()));
//
//		List<Payment> boletos = boletoRepository.findAll(spefications);
////		 atualizarStatus(boletos);
//		List<PayamentVO> boletosVO = new ArrayList<>();
//		boletos.forEach(b -> {
//			boletosVO.add(new PayamentVO(b));
//		});
//
//		return boletosVO;
//	}

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

		if (year == 0) {
			year = LocalDate.now().getYear();
		}
		if (month == 0) {
			month = LocalDate.now().getMonthValue();
		}

		LocalDate startMonth = LocalDate.of(year, month, 1);
		LocalDate endMonth = LocalDate.of(year, month, startMonth.lengthOfMonth());

		List<Payment> boletos = new ArrayList<Payment>();

		if (status.isEmpty()) {
			boletos = paymentRepository.findPayamentsInMonth(usersId, startMonth, endMonth);
		} else {
			boletos = paymentRepository.findForPaymentsByMonthAndStatus(usersId, startMonth, endMonth, status);
		}
		return boletos;
	}

	public List<Payment> findByDescricao(Long usersId, String descricao) {

		List<Payment> boletos = paymentRepository.findByDescriptionForUsers(usersId, descricao);
		return boletos;

	}

//	public void resumoDoMesXlsx(String nome) throws IOException {
//		List<Payment> boletos = new ArrayList<>();
//
//		List<PayamentVO> boletosVO = new ArrayList<>();
////				findBoletosDoMes(now().getMonthValue(), now().getYear());
//		boletosVO.forEach(b -> {
//			boletos.add(modelMapper.map(b, Payment.class));
//		});
//		Xlsx.planilhaBoletos(boletos, nome);
//		EmailDto email = new EmailDto();
//		email.setEmailTo("gustavo91010@gmail.com");
//		email.setSubject("testando micro servicos");
//		email.setText("opa lele, opa lala, ta na hora de pegar!");
//		email.setUser_id(1l);
//
//		emailClient.sendingEmail(email);
//		System.err.println(email.toString());
//	}

	// Efetivar Pagamento
	public Payment makePayment(Long id) {
		Payment boleto = findById(id);
		boleto.setStatus(StatusBoleto.PAGO);
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
