package com.sau.chatapp.sauchatapp.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    // Bağlı olan tüm client'ların listesi
    private List<WebSocketSession> connectedSessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Yeni bir client bağlandığında, bağlantı bilgilerini ekle
        connectedSessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Gelen mesajı diğer client'ların hepsine gönder
        for (WebSocketSession connectedSession : connectedSessions) {
            if (connectedSession.isOpen() && !connectedSession.getId().equals(session.getId())) {
                connectedSession.sendMessage(new TextMessage(message.getPayload().toString()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // Bir client bağlantısı kesildiğinde, bağlantı bilgilerini sil
        connectedSessions.remove(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Bir hata oluştuğunda
        if (session.isOpen()) {
            session.close();
        }
        connectedSessions.remove(session);
    }

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

}