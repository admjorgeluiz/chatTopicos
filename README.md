# Comunicação entre Processos em Sistemas Distribuídos

Este repositório apresenta um projeto de sistema de chat desenvolvido como atividade acadêmica da disciplina **Tópicos Especiais em Computação**, ministrada na **Universidade Tiradentes**.

## 📘 Sobre o Projeto

O projeto consiste na construção de um sistema de comunicação entre processos, inicialmente utilizando **Sockets TCP** com Java e posteriormente migrando para uma arquitetura moderna baseada em **WebSockets com Spring Boot**.

### 🎯 Objetivos
- Estudar e aplicar a comunicação entre processos em sistemas distribuídos.
- Implementar sockets TCP com uso eficiente de **ThreadPool (ExecutorService)**.
- Evoluir para WebSocket com suporte a múltiplos clientes via navegador.
- Documentar todo o processo em relatório técnico conforme normas da **ABNT**.

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot
- WebSocket API
- HTML5 / JavaScript
- Maven

## 📁 Estrutura do Projeto

- `/src` - Códigos-fonte do backend (Java/Spring Boot)
- `/resources/static` - Frontend com HTML e JS

## 🚀 Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   ```

2. Navegue até o diretório e execute com Maven:
   ```bash
   cd websocket-chat
   mvn spring-boot:run
   ```

3. Acesse no navegador:
   ```
   http://localhost:8080/index.html
   ```

## 🧪 Testes Realizados

- Execução em múltiplos navegadores e dispositivos
- Envio e recebimento de mensagens em tempo real
- Validação do uso de ThreadPool na versão com TCP

## 👥 Autores

- Anthony Mendes da Silva
- Breno Mateus Martins Barreto
- Gabriel Soares
- Gustavo Moreira Santos
- Jorge Luiz Machado Nascimento
- Leonardo Henrique Lopes Cardozo

## 🎓 Orientação

**Prof. Felipe dos Anjos Lima**

---

> Projeto acadêmico — Universidade Tiradentes | Aracaju — 2025
