package com.ajudaqui.bill.manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajudaqui.bill.manager.entity.Users;
import com.ajudaqui.bill.manager.entity.Vo.UsersVO;
import com.ajudaqui.bill.manager.exception.NotFoundEntityException;
import com.ajudaqui.bill.manager.repository.UsersRepository;

@Service
public class UsersService {
	@Autowired
	private UsersRepository usersRepository;

	public Users create(UsersVO usersVO) {
		Users users = new Users();
		users.setName(usersVO.getName());
		users.setEmail(usersVO.getEmail());
		users.setActive(true);
		users.setCreated_at(LocalDateTime.now());
		users.setUpdated_at(users.getCreated_at());
		users = usersRepository.save(users);
		return users;
		
		

	}

	public Users findById(Long id) {
		Users users = usersRepository.findById(id).get();
		return users;
	}

	public List<Users> findAll() {
		return usersRepository.findAll();

	}

	public Users findByEmail(String email) {
		System.err.println("email: "+email);
		 Optional<Users> users = usersRepository.findByEmail(email);
		 if (!users.isPresent()) {
				throw new NotFoundEntityException("Usuario não encontrado.");
			}
		System.err.println("users.get() "+users.get());
		return users.get();

	}

	public void delete(Long id) {
		
		usersRepository.deleteById(id);

	}

}
