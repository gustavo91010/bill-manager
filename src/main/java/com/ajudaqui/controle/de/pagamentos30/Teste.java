package com.ajudaqui.controle.de.pagamentos30;

import java.util.List;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Teste {
	public static void main(String[] args) {
		String json= "[\r\n"
				+ "    {\r\n"
				+ "        \"id\": 18,\r\n"
				+ "        \"descricao\": \"nubank\",\r\n"
				+ "        \"valor\": 1438.23,\r\n"
				+ "        \"vencimento\": \"2023-05-24\",\r\n"
				+ "        \"status\": \"EM_DIAS\"\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "        \"id\": 19,\r\n"
				+ "        \"descricao\": \"luz\",\r\n"
				+ "        \"valor\": 238.23,\r\n"
				+ "        \"vencimento\": \"2023-05-30\",\r\n"
				+ "        \"status\": \"EM_DIAS\"\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "        \"id\": 1,\r\n"
				+ "        \"descricao\": \"carro\",\r\n"
				+ "        \"valor\": 1537.93,\r\n"
				+ "        \"vencimento\": \"2023-05-14\",\r\n"
				+ "        \"status\": \"PAGO\"\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "        \"id\": 10,\r\n"
				+ "        \"descricao\": \"escola\",\r\n"
				+ "        \"valor\": 180.00,\r\n"
				+ "        \"vencimento\": \"2023-05-10\",\r\n"
				+ "        \"status\": \"PAGO\"\r\n"
				+ "    }\r\n"
				+ "]";
		
//		System.out.println(json);
		Gson gson = new Gson();
		TypeToken<List<Boleto>> typeToken = new TypeToken<List<Boleto>>() {};
		List<Boleto> boletos = gson.fromJson(json, typeToken.getType());
	}

}


