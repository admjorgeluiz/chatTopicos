package com.example.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TcpServerRunner implements CommandLineRunner {

    private static final int PORT = 12345;
    private static final Set<PrintWriter> clientes = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void run(String... args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("🟢 Servidor TCP rodando na porta " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            pool.execute(new ClientHandler(socket));
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String nomeUsuario;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                clientes.add(out);

                nomeUsuario = in.readLine();
                System.out.println("🟡 " + nomeUsuario + " entrou via TCP.");
                broadcast("🟢 " + nomeUsuario + " entrou no chat (TCP)");

                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                    if (!mensagem.trim().isEmpty()) {
                        System.out.println("[" + nomeUsuario + "]: " + mensagem);
                        broadcast("[" + nomeUsuario + "]: " + mensagem);
                    }
                }
            } catch (Exception e) {
                System.out.println("❌ Conexão encerrada com " + nomeUsuario);
            } finally {
                try {
                    socket.close();
                } catch (Exception ignored) {}
                clientes.remove(out);
                if (nomeUsuario != null) {
                    broadcast("🔴 " + nomeUsuario + " saiu do chat (TCP)");
                }
            }
        }

        private void broadcast(String mensagem) {
            synchronized (clientes) {
                for (PrintWriter cliente : clientes) {
                    cliente.println(mensagem);
                }
            }
        }
    }
}
