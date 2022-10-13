package com.ajudaqui.controle.de.pagamentos30.config;

import java.util.Collections;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	public Docket controleDePagamentosApi() {
//		http://localhost:8080/swagger-ui.html

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.ajudaqui.controle-de-pagamentos-3.0"))// o endereco de onde ele vai comecar a leitura
				.paths(PathSelectors.ant("/**"))// se tivesse algum documento restrito estariao aqui, como nao ha, esta tudo liberado **
				.build()
				.apiInfo(apiInfo());
//				.ignoredParameterTypes(null)// se eu quisesse ignorar os endpoits de alguma classe, ela estaria qui
		
	};
	private ApiInfo apiInfo() {
		String title= "Controle de pagamentos 3.0";
		String description= "uma api para controle de pagamentos de contas.";
		String version= "V3";

		Contact contato=   new Contact("Gustavo Paes", "Site legal", "gustavo910@gmail.com");
		ApiInfo apiInfo= new ApiInfo(""
				+title,
				description,
				version,
				null,
				contato,
				"lembro nao",
				"nem desse|",
				Collections.emptyList());
		return apiInfo;
	}

}
