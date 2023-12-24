package com.ajudaqui.billmanager.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.service.vo.PayamentDto;
import com.ajudaqui.billmanager.utils.StatusBoleto;
import com.ajudaqui.billmanager.utils.ValidarStatus;

@Service
public class PayamentService {

	@Autowired
	private PaymentsRepository boletoRepository;
	@Autowired
	private UsersService usersService;
	
//	@Autowired
//	private ModelMapper modelMapper;
//
//	@Autowired
//	private EmailClient emailClient;

	public Payment cadastrar(PayamentDto paymentDto, Long userId) {
		Users users = usersService.findById(userId);
		
//		payment already registered
		
		List<Payment> paymentForMonth = findAllMonth(userId, paymentDto.getDue_date().getMonthValue(), paymentDto.getDue_date().getYear());

		boolean alrreadRegistered = paymentForMonth.stream()
		    .anyMatch(
		    p -> p.getDescription().equals(paymentDto.getDescription())
		     && p.getValue().equals(paymentDto.getValue()));
		if(alrreadRegistered) {
			throw new MsgException("pagamento já cadastrado");
		}
		
		return boletoRepository.save(paymentDto.toDatabase(boletoRepository, users));

	}

	public void boletosRecorrentes(PayamentDto boletoDto, Long repeticao, Long userId) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		for (int i = 1; i < repeticao; i++) {

			LocalDate vencimento = LocalDate.parse(boletoDto.getDue_date().toString(), formatter);
			boletoDto.setDue_date(vencimento);
			cadastrar(boletoDto,userId);
		}

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

	public Payment findById(Long id) {

		Payment boleto = boletoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Boleto não encontrado."));
		return boleto;
	}

	public List<Payment> findByPayamentsForUser(Long userId) {

		return boletoRepository.findByPayamentsForUser(userId);
	}
	
	public List<Payment>  findAllMonth(Long userId, Integer month, Integer year) {
		LocalDate startMonth = LocalDate.of(year, month, 1);
		LocalDate endMonth = LocalDate.of(year, month, startMonth.lengthOfMonth());
		List<Payment> resultt = boletoRepository.findAllMonth(userId, startMonth, endMonth);
		return resultt;
		
		
	}

	public List<Payment> searcheByMonthAndStatus(Long id, Integer month, Integer year, String status) {
		System.err.println("service");

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
			boletos = boletoRepository.findPayamentsInMonth(startMonth, endMonth);
		} else {
			boletos = boletoRepository.findForPaymentsByMonthAndStatus(startMonth, endMonth, status);
		}
		return boletos;
	}

	public List<PayamentDto> findByDescricao(String descricao) {

		List<Payment> boletos = new ArrayList<>();
		List<PayamentDto> boletosVO = new ArrayList<>();
		boletos.forEach(b -> {
			boletosVO.add(new PayamentDto(b));
		});

		return boletosVO;
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
		boletoRepository.save(boleto);
		return boleto;

	}

	// actualização do estado em execução
	public void performStatusUpdate() {
		List<Payment> pagamentos = boletoRepository.nextPayments(LocalDate.now().plusDays(10));
		atualizarStatus(pagamentos);
	}

	public List<Payment> atualizarStatus(List<Payment> boletos) {
		boletos.forEach(b -> {
			ValidarStatus.statusAtualizado(b, boletoRepository);

		});
		return boletos;

	}

	public void deleteById(Long id) {
		Payment boleto = findById(id);
		boletoRepository.delete(boleto);

	}

}
