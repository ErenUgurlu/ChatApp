package com.sau.chatapp.sauchatapp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sau.chatapp.sauchatapp.entities.MessageDto;
import com.sau.chatapp.sauchatapp.entities.User;
import com.sau.chatapp.sauchatapp.repositories.MessageRepository;
import com.sau.chatapp.sauchatapp.services.UserService;
import com.sau.chatapp.sauchatapp.util.EncryptionUtil;

@Controller
public class HomeController {

	@Autowired
	UserService userService;

	@Autowired
	MessageRepository messageRepository;
	
	@Autowired
	EncryptionUtil encryptionUtil;

	@GetMapping("/")
	String home(Model model, Authentication authentication) {
		String userName = authentication.getName();
		List<MessageDto> allMessages = messageRepository.findAll();
		List<MessageDto> userMessages = new ArrayList<MessageDto>();
		
		for (MessageDto messageDto : allMessages) {
			if(messageDto.getSender().equals(userName) || messageDto.getReceiver().equals(userName) || messageDto.getReceiver().equals("main")) {
				try {
//					messageDto.setMessage(EncryptionUtil.decrypt(messageDto.getMessage(), encryptionUtil.chatKey(messageDto.getReceiver(), messageDto.getSender())));
				} catch (Exception e) {
					
					System.err.println("Şifre çözme sırasında home controllerda hata yaşandı. Hata: "+e);
				}
				userMessages.add(messageDto);
			}
		}
		
		List<User> userList = userService.getAllUsers();
		model.addAttribute("userName", userName);
		model.addAttribute("userList", userList);
		model.addAttribute("userMessages",userMessages);
		return "index";
	}
	
	

	@GetMapping("/signin")
	String signIn() {
		return "signin";
	}

	@GetMapping("/signup")
	String signUp() {
		return "signup";
	}
}
