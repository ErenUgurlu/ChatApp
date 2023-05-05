package com.sau.chatapp.sauchatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.sau.chatapp.sauchatapp.entities.MessageDto;

@Controller
@CrossOrigin
public class ChatController {

	@Autowired
	SimpMessagingTemplate messagingTemplate;
	
	/**
	 * @SendTo ile gelen mesajın hangi kanala gönderileceği seçilir @SendToUser ile belirli bir kullaniciya mesaj gonderilebilir.
	 * Bunlarla aynı işlevde olan messaging template kullanmayı tercih ettik burada daha rahat özelleştirme yapılabiliyor.
	 */
    @MessageMapping("/chat")
//    @SendToUser()
//    @SendTo("/topic")
    public void send(String message) throws Exception {
        // mesaj işleme işlemleri
    	//chat endpointine gelen bir mesajı butun clientlere gönderiyor
    	System.out.println("message: " + message);
    	messagingTemplate.convertAndSend("/topic", message);
    }
}
