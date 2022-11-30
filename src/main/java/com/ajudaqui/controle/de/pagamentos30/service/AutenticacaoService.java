package com.ajudaqui.controle.de.pagamentos30.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajudaqui.controle.de.pagamentos30.entity.Usuario;
import com.ajudaqui.controle.de.pagamentos30.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if(usuario.isEmpty()) {
			throw new UsernameNotFoundException("dados invalidos");
		}

		return usuario.get();
	}
//	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("123456"));
////		$2a$10$StBGModVqnAcZBaGj/tuOumphk8H.1vUJceQC5Pr6YbpN.Z2k9xry
//
//	}

}
