package com.ajudaqui.bill.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajudaqui.bill.manager.entity.Payament;
import com.ajudaqui.bill.manager.entity.Users;
import com.ajudaqui.bill.manager.entity.Vo.PayamentVO;
import com.ajudaqui.bill.manager.entity.Vo.UsersVO;
import com.ajudaqui.bill.manager.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private UsersService usersService;

	@PostMapping()
	public ResponseEntity<?> create(@RequestBody UsersVO userVo) {
		Users users = usersService.create(userVo);

		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") Long id) {
		try {

			Users users = usersService.findById(id);
			return ResponseEntity.ok(users);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("OUsuario não encontrado");
		}

	}
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<?> findByEmail(@PathVariable("email") String email) {
		try {

			Users users = usersService.findByEmail(email);
			return ResponseEntity.ok(users);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("OUsuario não encontrado");
		}

	}
	@GetMapping()
	public ResponseEntity<?> findAll() {
		try {

			 List<Users> users = usersService.findAll();
			return ResponseEntity.ok(users);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("OUsuario não encontrado");
		}

	}
	
	@DeleteMapping(value = "/id/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		try {

			 usersService.delete(id);
			return ResponseEntity.ok("Usuario deletado.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Usuario não encontrado");
		}

	}

}
