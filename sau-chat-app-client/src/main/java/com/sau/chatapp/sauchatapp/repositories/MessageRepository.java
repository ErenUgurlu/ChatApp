package com.sau.chatapp.sauchatapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sau.chatapp.sauchatapp.entities.MessageDto;

@Repository
public interface MessageRepository extends JpaRepository<MessageDto, Long> {
	List<MessageDto> findBySender(String sender);
	List<MessageDto> findByReceiver(String receiver);
}
