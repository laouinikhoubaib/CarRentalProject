package com.example.carrental.Controllers;
import com.example.carrental.Models.ChatMessage;
import com.example.carrental.Models.Chatroom;
import com.example.carrental.Repository.ChatmessageRepo;
import com.example.carrental.Repository.ChatroomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chatt")
public class ChatController {
	@Autowired
    ChatmessageRepo mr;
	@Autowired
    ChatroomRepo cr;
	

    @MessageMapping("/sendmsg")

    @SendTo("/chat/messages")
    public ChatMessage chat(ChatMessage message) throws Exception {
        Thread.sleep(1000);
        Chatroom ch = cr.findById(message.getIdchat()).orElse(null);
        message.setChat(ch);
        mr.save(message);
        return new ChatMessage(message.getMessageId(),message.getText(), message.getUsername(), message.getAvatar(),message.getSender(),message.getIdchat(),message.getChat());
    }
    
}
