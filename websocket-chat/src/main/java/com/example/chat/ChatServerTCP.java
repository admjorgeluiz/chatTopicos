package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerTCP {
    private static final int PORT = 12345;
    private static final Set<PrintWriter> clientes = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor TCP rodando na porta " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                pool.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String nome;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                clientes.add(out);

                nome = in.readLine();
                System.out.println("🟢 " + nome + " entrou no chat.");
                broadcast("🟢 " + nome + " entrou no chat.");

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (!msg.trim().isEmpty()) {
                        System.out.println("[" + nome + "]: " + msg);
                        broadcast("[" + nome + "]: " + msg);
                    }
                }
            } catch (IOException e) {
                System.out.println("❌ Conexão perdida com " + nome);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
                clientes.remove(out);
                broadcast("🔴 " + nome + " saiu do chat.");
            }
        }

        private void broadcast(String msg) {
            synchronized (clientes) {
                for (PrintWriter cliente : clientes) {
                    cliente.println(msg);
                }
            }
        }
    }
}
