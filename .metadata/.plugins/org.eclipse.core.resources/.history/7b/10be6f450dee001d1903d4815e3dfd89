package com.ajudaqui.controle.de.pagamentos30.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ajudaqui.controle.de.pagamentos30.service.AutenticacaoService;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@Configurable
public class SecutityConfigurations extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AutenticacaoService service;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service)
		.passwordEncoder(new BCryptPasswordEncoder());
		
//		super.configure(auth);
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/boletos").permitAll()
		.antMatchers(HttpMethod.GET, "/boletos/id/*").permitAll()
		.anyRequest().authenticated()
		
		.and().formLogin();
		
		
	}
}
