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
		Chatroom cht = null;

		if (chatroomRepo != null && idSender != null && idReceiver != null) {
			for (Chatroom ch : chatroomRepo.findAll()) {
				User sender = ch.getSender();
				User receiver = ch.getReciver();
				if (sender != null && receiver != null) {
					Long senderId = sender.getUserId();
					Long receiverId = receiver.getUserId();
					if ((receiverId.equals(idReceiver) && senderId.equals(idSender)) ||
							(receiverId.equals(idSender) && senderId.equals(idReceiver))) {
						cht = ch;
						break;
					}
				}
			}
		}

		if (cht != null) {
			return cht;
		} else {
			User sender = userRepository.findById(idSender).orElse(null);
			User receiver = userRepository.findById(idReceiver).orElse(null);
			if (sender != null && receiver != null) {
				Chatroom newc = new Chatroom();
				newc.setSender(sender);
				newc.setReciver(receiver);
				return chatroomRepo.save(newc);
			} else {
				throw new IllegalArgumentException("Invalid sender or receiver IDs");
			}
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
