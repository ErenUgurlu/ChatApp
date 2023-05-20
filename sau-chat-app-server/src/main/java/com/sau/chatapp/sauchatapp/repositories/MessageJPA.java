package com.sau.chatapp.sauchatapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sau.chatapp.sauchatapp.entities.MessageDto;


public interface MessageJPA extends JpaRepository<MessageDto, Long>{

}
