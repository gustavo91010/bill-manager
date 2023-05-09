package com.ajudaqui.controle.de.pagamentos30.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajudaqui.controle.de.pagamentos30.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Optional<Usuario> findByEmail(String email);

}
