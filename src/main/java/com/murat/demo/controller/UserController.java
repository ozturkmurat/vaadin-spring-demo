package com.murat.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.murat.demo.model.User;
import com.murat.demo.persistence.UserRepository;
import com.murat.demo.util.PasswordUtil;

@Controller
@RequestMapping(path = "/userapi")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(path = "/add")
	public @ResponseBody String addNewUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String password, @RequestParam String email) {

		for(User user : userRepository.findAll()){
			if(user.getEmail().equals(email)){
				return "E-mail is already is in use!";
			}
		}
				User newUser = new User();
				newUser.setFirstName(firstName);
				newUser.setLastName(lastName);
				newUser.setPassword(PasswordUtil.cryptWithMD5(password));
				newUser.setEmail(email);
				newUser.setUuid(UUID.randomUUID().toString());
				userRepository.save(newUser);
		
		return "User Registered Succesfully";

	}
	
	
}
