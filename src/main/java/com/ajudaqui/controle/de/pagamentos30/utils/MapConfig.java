package com.ajudaqui.controle.de.pagamentos30.utils;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapConfig {
	@Bean
	public ModelMapper obterModelMapper() {
		return new ModelMapper();
	}

}
