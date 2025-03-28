package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class ChatClient {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        String serverIp = detectarServidor();
        if (serverIp == null) {
            System.out.println("Não foi possível detectar o servidor automaticamente.");
            System.out.print("Digite o IP do servidor: ");
            try (BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
                serverIp = teclado.readLine();
            } catch (IOException e) {
                System.err.println("Erro ao ler o IP do servidor.");
                return;
            }
        }

        System.out.println("Conectando ao servidor em: " + serverIp);

        try (
            Socket socket = new Socket(serverIp, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.print("Digite seu nome: ");
            String nome = teclado.readLine();
            out.println(nome); // envia o nome para o servidor

            System.out.println("Conectado ao chat. Digite sua mensagem:");

            Thread leitor = new Thread(() -> {
                try {
                    String resposta;
                    while ((resposta = in.readLine()) != null) {
                        System.out.println(resposta);
                    }
                    System.out.println("❌ Conexão encerrada pelo servidor.");
                    System.exit(0);
                } catch (IOException e) {
                    System.out.println("❌ Erro na leitura do servidor.");
                    System.exit(0);
                }
            });

            leitor.start();

            String mensagem;
            while ((mensagem = teclado.readLine()) != null) {
                if (!mensagem.trim().isEmpty()) {
                    out.println(mensagem);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao conectar-se ao servidor:");
            e.printStackTrace();
        }
    }

    private static String detectarServidor() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.isSiteLocalAddress()) {
                        String[] partes = address.getHostAddress().split("\\.");
                        if (partes.length == 4) {
                            String subnet = partes[0] + "." + partes[1] + "." + partes[2] + ".";
                            for (int i = 1; i <= 254; i++) {
                                String ip = subnet + i;
                                try (Socket socket = new Socket()) {
                                    socket.connect(new InetSocketAddress(ip, PORT), 100);
                                    return ip;
                                } catch (IOException ignored) {}
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
