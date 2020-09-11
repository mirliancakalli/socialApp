package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;

@RestController
@RequestMapping(value = "/api")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDao userDao;

	@GetMapping(value = "/users")
	public List<User> getAllUsers() {
		logger.error("getting all users");
		return userDao.findAll();
	}

	@DeleteMapping(value = "/user/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		logger.error("deleting user =" + userId);
		User user = userDao.findByPk(userId);
		if (user != null && user.getId() != null) {
			userDao.delete(user);
			return ResponseEntity.status(HttpStatus.OK).body("User deleted Successfylly : " + user.toString());
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("User not deleted Successfylly : " + user.toString());
		}
	}

	@GetMapping(value = "/user/{idUser}")
	public User getUserById(@PathVariable(value = "idUser") Long idUser) {
		logger.info("getting user with id=" + idUser);
		return userDao.findByPk(idUser);
	}

	@PostMapping(value = "/users")
	public ResponseEntity<?> insertUser(@RequestBody User user) {
		logger.info("start saving the new user");
		try {
			User newUser = userDao.save(user);
			return ResponseEntity.status(HttpStatus.OK).body("User inserted Successfylly : " + newUser.toString());
		} catch (Exception e) {
			logger.error("user could not be inserted ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("User not inserted Successfylly : " + e.getMessage());
		}
	}

	@PutMapping(value = "/user")
	public ResponseEntity<?> modifyUser(@RequestBody User user) {
		Map<String, String> response = new HashMap<>();
		try {
			User userExists = userDao.findByPk(user.getId());
			if (userExists != null) {
				userDao.save(user);
				logger.info("user updated successfylly");
				return ResponseEntity.status(HttpStatus.OK).body(user);
			} else {
				logger.info("user not exists" + user.getId().toString());
				response.put("response", "false");
				response.put("User does not exists in database", user.toString());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("user could not be updated", e);
			response.put("response", "false");
			response.put("response", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/user/pagi")
	public ResponseEntity<?> getAllPostPaginated(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			Pageable paging = PageRequest.of(page, size);
			Page<User> pageTuts = userDao.findAll(paging);

			Map<String, Object> response = new HashMap<>();
			response.put("users", pageTuts);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
