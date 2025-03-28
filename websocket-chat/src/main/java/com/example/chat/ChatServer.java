package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatServer {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clientes = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        SpringApplication.run(ChatServer.class, args);
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Servidor iniciado...");
            System.out.println("Endereço IP: " + localHost.getHostAddress());

            try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("0.0.0.0"))) {
                while (true) {
                    new ClientHandler(serverSocket.accept()).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String nomeUsuario;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Armazena o escritor do cliente
                clientes.add(out);

                // Recebe o nome do usuário
                nomeUsuario = in.readLine();
                System.out.println(nomeUsuario + " entrou no chat.");
                broadcast("🟢 " + nomeUsuario + " entrou no chat.");

                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                    if (!mensagem.trim().isEmpty()) {
                        System.out.println("[" + nomeUsuario + "]: " + mensagem);
                        broadcast("[" + nomeUsuario + "]: " + mensagem);
                    }
                }
            } catch (IOException e) {
                System.out.println("Conexão com " + nomeUsuario + " perdida.");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                clientes.remove(out);
                if (nomeUsuario != null) {
                    System.out.println(nomeUsuario + " saiu do chat.");
                    broadcast("🔴 " + nomeUsuario + " saiu do chat.");
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
