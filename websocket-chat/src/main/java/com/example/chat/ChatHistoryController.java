package com.example.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatHistoryController {

    @GetMapping("/historico")
    public List<String> getHistorico() {
        return ChatHandler.getHistorico();
    }
}
