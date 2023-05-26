package com.sau.chatapp.sauchatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sau.chatapp.sauchatapp.entities.User;
import com.sau.chatapp.sauchatapp.security.JwtUtils;
import com.sau.chatapp.sauchatapp.services.UserService;
import com.sau.chatapp.sauchatapp.util.EncryptionUtil;

@RestController
public class KeyController {

	@Autowired
	UserService userService;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	EncryptionUtil encryptionUtil;

	@PostMapping("/key")
	String getKey(@RequestParam("receiver") String receiver, @RequestParam("sender") String sender,
			@RequestParam("jwt") String jwt) throws Exception {
		String encryptedKey = encryptionUtil.chatKey(receiver, sender, jwt);
		
		return encryptedKey;
	}
}
