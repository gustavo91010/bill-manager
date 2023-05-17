package com.ajudaqui.controle.de.pagamentos30.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;

public class Xlsx {

	public static Path planilhaBoletos(List<Boleto> boletos, String nome) throws IOException {
		// cria uma nova planilha
		Workbook planilha = new XSSFWorkbook();

		Sheet folha = planilha.createSheet("Boletos do mes " + boletos.get(0).getVencimento().getMonth());
		int posicaoLinha = 1;
		Cell celula;
		Row linha = folha.createRow(posicaoLinha++);
		// Linha de atributo:
		celula = linha.createCell(0);
		celula.setCellValue("id");
		folha.autoSizeColumn(0);
		
		celula = linha.createCell(1);
		celula.setCellValue("descricao");
		folha.autoSizeColumn(1);
		
		celula = linha.createCell(2);
		celula.setCellValue("valor");
		folha.autoSizeColumn(2);
		
		celula = linha.createCell(3);
		celula.setCellValue("vencimento");
		folha.autoSizeColumn(3);
		
		celula = linha.createCell(4);
		celula.setCellValue("status");
		folha.autoSizeColumn(4);

		for (Boleto boleto : boletos) {
			linha = folha.createRow(posicaoLinha++);

			// Linha de valores:
			celula = linha.createCell(0);
			celula.setCellValue(boleto.getId());
			celula = linha.createCell(1);
			
			celula.setCellValue(boleto.getDescricao());
			celula = linha.createCell(2);
			
			celula.setCellValue(boleto.getValor().toString());
			celula = linha.createCell(3);
			folha.autoSizeColumn(3);
			
			celula.setCellValue(boleto.getVencimento().toString());
			celula = linha.createCell(4);
			celula.setCellValue(boleto.getStatus().toString());
			folha.autoSizeColumn(4);

		}
		Path path = escreverPanilha(planilha,boletos, nome);
		return path.toAbsolutePath(); 

	}
	private static Path escreverPanilha(Workbook planilha,List<Boleto> boletos, String nome) throws IOException {
		// Retira caracters especiais que nao podem estar no titulo da planilha
		String userHome = System.getProperty("user.home");
		String subDir = "controle-de-pagamentos";
		Path pat0h = Paths.get(userHome, subDir);
		
		Path path = pat0h.resolve(nome + ".xlsx");
		
		// Cria o diretório com o título
		if (!Files.exists(pat0h)) {
			Files.createDirectories(pat0h);
		}
		
		FileOutputStream arquivoSaida = new FileOutputStream(path.toString());
		planilha.write(arquivoSaida);
		arquivoSaida.flush();
		

		System.out.println("Seu arquivo se encontra em:\n" + path.toAbsolutePath());

	return path;

}

}
