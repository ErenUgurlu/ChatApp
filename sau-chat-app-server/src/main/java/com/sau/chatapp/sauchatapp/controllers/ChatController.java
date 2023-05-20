package com.sau.chatapp.sauchatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.google.gson.Gson;
import com.sau.chatapp.sauchatapp.entities.MessageDto;
import com.sau.chatapp.sauchatapp.repositories.MessageJPA;

@Controller
@CrossOrigin
public class ChatController {

	@Autowired
	SimpMessagingTemplate messagingTemplate;

	@Autowired
	MessageJPA messageJPA;

	/**
	 * @SendTo ile gelen mesajın hangi kanala gönderileceği seçilir @SendToUser ile
	 *         belirli bir kullaniciya mesaj gonderilebilir. Bunlarla aynı işlevde
	 *         olan messaging template kullanmayı tercih ettik burada daha rahat
	 *         özelleştirme yapılabiliyor.
	 */

//  @SendToUser()
//  @SendTo("/topic")
	@MessageMapping("/chat")
	public void send(String message) throws Exception {
		// mesaj işleme işlemleri
		// chat endpointine gelen bir mesajı butun clientlere gönderiyor
		System.out.println("message: " + message);
		Gson gson = new Gson();
		messageJPA.save(gson.fromJson(message, MessageDto.class));

		System.out.println("message: " + message);
		messagingTemplate.convertAndSend("/topic", message);
	}
}
