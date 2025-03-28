import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalTime;
import java.util.*;

public class ChatHandler extends TextWebSocketHandler {

    // Sessões dos clientes conectados
    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    // Histórico das mensagens (em memória)
    private static final List<String> historico = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);

        // Enviar o histórico de mensagens para o novo cliente
        synchronized (historico) {
            for (String msg : historico) {
                try {
                    session.sendMessage(new TextMessage(msg));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String horario = LocalTime.now().withNano(0).toString(); // Horário sem nanossegundos
        String conteudo = message.getPayload();

        // Mensagem formatada com horário
        String mensagemFormatada = "[" + horario + "] " + conteudo;

        // Armazenar no histórico
        historico.add(mensagemFormatada);

        // Enviar para todos os clientes conectados
        synchronized (sessions) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(mensagemFormatada));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
