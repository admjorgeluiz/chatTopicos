
# 💬 Chat WebSocket com Spring Boot

Este projeto é um sistema de chat em tempo real utilizando WebSocket, desenvolvido com **Spring Boot**, HTML e JavaScript. Ele permite que múltiplos usuários se conectem, enviem mensagens, visualizem o histórico e acompanhem em tempo real a entrada e saída de outros usuários.

## 🚀 Funcionalidades

- Comunicação em tempo real via WebSocket
- Exibição de nome, data e hora em cada mensagem
- Histórico persistido em memória (últimas 100 mensagens)
- Rota HTTP `/historico` para visualizar o histórico como JSON
- Interface separada e estilizada para visualizar o histórico
- Notificações de entrada e saída de usuários
- Layout responsivo com frontend simples em HTML + CSS

## 🌐 Acesso online

Aplicação hospedada na Railway:

🔗 [https://chattopicos-production.up.railway.app](https://chattopicos-production.up.railway.app)

## 📁 Estrutura

```
├── src
│   ├── main
│   │   ├── java/com/example/chat
│   │   │   ├── ChatClient.java             # Cliente via socket TCP (terminal)
│   │   │   ├── ChatHandler.java            # WebSocket handler
│   │   │   ├── ChatHistoryController.java  # Rota REST para histórico
│   │   │   ├── ChatServer.java             # Versão legacy com socket TCP (sem Spring)
│   │   │   ├── ChatServerTCP.java          # Servidor TCP independente com ThreadPool
│   │   │   ├── TcpServerRunner.java        # Runner do servidor TCP embutido no Spring
│   │   │   └── WebSocketConfig.java        # Configuração do endpoint WebSocket
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   │           ├── index.html              # Interface principal do chat
│   │           └── historico.html          # Visualização separada do histórico
```

## 🧠 Tecnologias Utilizadas

- Java 17
- Spring Boot
- WebSocket
- HTML5 + CSS3 + JavaScript
- Railway (deploy)

## 🔌 Como executar localmente

1. Clone o repositório:

```bash
git clone https://github.com/SEU_USUARIO/chat-websocket.git
cd chat-websocket
```

2. Compile e execute com Maven:

```bash
./mvnw spring-boot:run
```

3. Acesse:

```
http://localhost:8080/index.html
```

Ou o histórico:

```
http://localhost:8080/historico.html
```

## 🛠️ Melhorias futuras

- Persistência do histórico em banco de dados
- Sistema de autenticação
- Comandos especiais no chat (ex: /ajuda, /limpar)
- Painel de administração

## 👨‍💻 Desenvolvedores

- Anthony Mendes da Silva  
- Breno Mateus Martins Barreto  
- Gabriel Soares  
- Gustavo Moreira Santos  
- Jorge Luiz Machado Nascimento  
- Leonardo Henrique Lopes Cardozo  

## 📝 Licença

Este projeto é de uso educacional e livre para fins de aprendizado.


## 👨‍🏫 Professor Orientador

- Felipe dos Anjos Lima
