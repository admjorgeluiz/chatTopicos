package com.example.chat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private static final List<String> historico = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);

        // Envia o histórico para o novo cliente
        synchronized (historico) {
            for (String msg : historico) {
                session.sendMessage(new TextMessage(msg));
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Se o nome não estiver na sessão, extrai da primeira mensagem
        if (!session.getAttributes().containsKey("nome")) {
            String payload = message.getPayload();
            String nome = payload.substring(payload.indexOf('[') + 1, payload.indexOf(']')).trim();
            session.getAttributes().put("nome", nome);

            String aviso = "⚠️ " + nome + " entrou no chat";
            historico.add(aviso);
            enviarParaTodos(aviso);
        }

        String nome = (String) session.getAttributes().get("nome");

        // Formatar data e hora
        ZonedDateTime agora = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        String horario = agora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        // Extrair conteúdo da mensagem (remover o prefixo [nome]:)
        String conteudo = message.getPayload().substring(message.getPayload().indexOf("]:") + 2).trim();
        String mensagemFormatada = "[" + horario + "] " + nome + ": " + conteudo;

        synchronized (historico) {
            historico.add(mensagemFormatada);
            if (historico.size() > 100) {
                historico.remove(0);
            }
        }
        
        enviarParaTodos(mensagemFormatada);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);

        String nome = (String) session.getAttributes().get("nome");
        if (nome != null) {
            String aviso = "❌ " + nome + " saiu do chat";
            historico.add(aviso);
            enviarParaTodos(aviso);
        }
    }

    private void enviarParaTodos(String mensagem) {
        synchronized (sessions) {
            for (WebSocketSession s : sessions) {
                try {
                    if (s.isOpen()) {
                        s.sendMessage(new TextMessage(mensagem));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<String> getHistorico() {
        return historico;
    }
}
