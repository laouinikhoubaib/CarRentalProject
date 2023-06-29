package com.example.carrental.Controllers;

import com.example.carrental.websocketproject.ChatMessage;
import com.example.carrental.websocketproject.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WSController {

    @Autowired
    private WSService service;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final ChatMessage message) {
        service.notifyFrontend(message.getText());
    }

    @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable final String id,
                                   @RequestBody final ChatMessage message) {
        service.notifyUser(id, message.getText());
    }
}
