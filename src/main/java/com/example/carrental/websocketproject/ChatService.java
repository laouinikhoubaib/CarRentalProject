package com.example.carrental.websocketproject;

import com.example.carrental.Models.User;
import com.example.carrental.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChatService {
	@Autowired
	private ChatroomRepo chatroomRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageRepository messageRepository;

	public Chatroom findChat(Long idSender, Long idReceiver) {
		int x = 0;
		Chatroom cht =  new Chatroom();
		if (chatroomRepo != null) {
			for (Chatroom ch : chatroomRepo.findAll()) {
				if ((ch.getReciver().getUserId() == idReceiver && ch.getSender().getUserId() == idSender) ||
						(ch.getReciver().getUserId() == idSender && ch.getSender().getUserId() == idReceiver)) {
					x = 1;
					cht = ch;
					break;
				}
			}
		}

		if (x == 1) {
			return cht;
		} else {
			Chatroom newc =  new Chatroom();
			newc.setSender(null);
			User sender = userRepository.findById(idSender).orElse(null);
			User receiver = userRepository.findById(idReceiver).orElse(null);
			newc.setReciver(receiver);
			newc.setSender(sender);
			return chatroomRepo.save(newc);
		}
	}

	public Chatroom getConversation(Long idChatroom) {
		return chatroomRepo.findById(idChatroom).orElse(null);
	}

	public void changeColor(Long id, String color) {
		Chatroom chatroom = chatroomRepo.findById(id).orElse(null);
		if (chatroom != null) {
			chatroom.setColor(color);
			chatroomRepo.save(chatroom);
		}
	}
}
