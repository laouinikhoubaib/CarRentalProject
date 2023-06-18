package com.example.carrental.Controllers;

import com.example.carrental.Models.User;
import com.example.carrental.Repository.UserRepository;
import com.example.carrental.websocketproject.ChatService;
import com.example.carrental.websocketproject.Chatroom;
import com.example.carrental.websocketproject.ChatroomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatControllerr {
	@Autowired
	ChatService cs ;
	@Autowired
	UserRepository ur ;

	@Autowired
	ChatroomRepo cr ;

	@GetMapping("/Chatroom/{Idsender}/{idreciver}")
	@ResponseBody
	public Chatroom chatfind(@PathVariable("Idsender") Long Idsender, @PathVariable("idreciver") Long idreciver) {
		return cs.findchat(Idsender, idreciver);
	}


	@PostMapping("/getc/{idreciver}")
	@ResponseBody
	public Chatroom getcon(@PathVariable("idreciver") Long idreciver) {
		return cs.getConv(idreciver);
	}

	@GetMapping("/ListUser")
	@ResponseBody
	public List<User> getListUser() {
		return ur.findAll();
	}

	@GetMapping("/allchat")
	@ResponseBody
	public List<Chatroom> getChat() {
		return cr.findAll();
	}

	@PostMapping("/color/{id}")
	@ResponseBody
	public void color(@PathVariable("id") Long id ,@RequestBody String c) {
		cs.changecolor(id, c);
	}

}