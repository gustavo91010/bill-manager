package com.ajudaqui.billmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.NotFoundEntityException;
import com.ajudaqui.billmanager.repository.UsersRepository;
import com.ajudaqui.billmanager.service.vo.UserUpdateVo;
import com.ajudaqui.billmanager.service.vo.UsersVO;

@Service
public class UsersService {
	@Autowired
	private UsersRepository usersRepository;
	
	public String opa() {
		String opa="opa!";
		System.out.println(opa);
		return opa;
	}

	public Users create(UsersVO usersVO) {
		 Optional<Users> emailVerification = usersRepository.findByEmail(usersVO.getEmail());
		
		 if (emailVerification.isPresent()) {
				throw new NotFoundEntityException("Usuario já cadastrado.");
			}
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
		
		 Optional<Users> users = usersRepository.findById(id);
		 if(!userExist(id)) {
				throw new NotFoundEntityException("Usuario não encontrado.");

		 }
		 
		return users.get();
	}
public boolean userExist(Long userID) {
	return usersRepository.existsById(userID);
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
	public Users update(Long id,UserUpdateVo userUpdateVo) {
		
		Users user = findById(id);
	
		if(userUpdateVo.getName() !=null) {
			user.setName(userUpdateVo.getName());
		}
		if(userUpdateVo.getEmail() !=null) {
			user.setEmail(userUpdateVo.getEmail());
		}
		usersRepository.save(user);
		return user;
	}
	public Users changeStats(Long id) {
		Users user = findById(id);
		user.setActive(!user.getActive());
		usersRepository.save(user);
		return user;
		
	}
	

	public void delete(Long id) {
		
		usersRepository.deleteById(id);

	}


}
