package com.sau.chatapp.sauchatapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sau.chatapp.sauchatapp.entities.User;
import com.sau.chatapp.sauchatapp.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	public User getUserByName(String username) {
		Optional<User> optUser = userRepository.findByUsername(username);
		return optUser.get();
	}

}
