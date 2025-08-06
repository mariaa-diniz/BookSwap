# 📖 BookSwap - Plataforma de Troca de Livros

![Status](https://img.shields.io/badge/status-concluído-green)

BookSwap é um software de desktop desenvolvido como projeto para a faculdade, com o objetivo de criar uma plataforma onde usuários podem cadastrar livros que desejam trocar e encontrar outros livros de seu interesse.

---

## 🚀 Funcionalidades Principais

- **Cadastro e Login de Usuários:** Sistema de autenticação seguro com senhas criptografadas.
- **Catálogo de Livros:** Visualização de todos os livros disponíveis na plataforma, com uma interface moderna em formato de cards.
- **Perfil de Usuário:** Cada usuário possui uma página de perfil com suas informações e sua "estante" de livros cadastrados.
- **Sistema de Trocas:** Fluxo completo para propor, receber, aceitar e recusar propostas de troca.
- **Gerenciamento de Livros:** Usuários podem adicionar novos livros à sua estante e remover livros existentes.
- **Sistema de Feedback:** Após uma troca ser concluída, os usuários podem avaliar a experiência.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Interface Gráfica:** JavaFX
- **Banco de Dados:** MySQL
- **Build e Dependências:** Apache Maven
- **IDE:** IntelliJ IDEA / NetBeans

---

## ⚙️ Como Executar o Projeto

**Pré-requisitos:**
- JDK 17 (ou superior)
- Apache Maven
- MySQL Server (rodando localmente)

**Passos:**
1. Clone este repositório: `git clone https://github.com/mariaa-diniz/BookSwap.git`
2. Configure o banco de dados executando o script SQL abaixo.
3. Certifique-se de que as credenciais do banco no arquivo `src/main/java/com/bookswap/util/ConexaoMySQL.java` estão corretas.
4. Navegue até a pasta raiz do projeto pelo terminal e execute o comando Maven para rodar a aplicação:
   ```bash
   mvn javafx:run
📜 Script do Banco de Dados (SQL)
O script abaixo cria todo o esquema do banco de dados necessário para a aplicação.

<details>
CREATE DATABASE IF NOT EXISTS bookswap;
USE bookswap;

DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS trocas;
DROP TABLE IF EXISTS livros;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
id INT AUTO_INCREMENT PRIMARY KEY,
nome VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL UNIQUE,
senha VARCHAR(255) NOT NULL,
data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE livros (
id INT AUTO_INCREMENT PRIMARY KEY,
id_usuario INT NOT NULL,
titulo VARCHAR(255) NOT NULL,
autor VARCHAR(255) NOT NULL,
genero VARCHAR(100),
descricao TEXT,
estado_conservacao ENUM('NOVO', 'SEMINOVO', 'BOM', 'COM_AVARIAS') NOT NULL,
foto_url VARCHAR(255),
disponivel BOOLEAN DEFAULT TRUE,
FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE trocas (
id INT AUTO_INCREMENT PRIMARY KEY,
id_livro_solicitado INT NOT NULL,
id_usuario_solicitante INT NOT NULL,
id_livro_ofertado INT NOT NULL,
id_usuario_ofertado INT NOT NULL,
status ENUM('PENDENTE', 'ACEITA', 'RECUSADA', 'CONCLUIDA', 'CANCELADA') NOT NULL,
data_proposta DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (id_livro_solicitado) REFERENCES livros(id),
FOREIGN KEY (id_usuario_solicitante) REFERENCES usuarios(id),
FOREIGN KEY (id_livro_ofertado) REFERENCES livros(id),
FOREIGN KEY (id_usuario_ofertado) REFERENCES usuarios(id)
);

CREATE TABLE feedbacks (
id INT AUTO_INCREMENT PRIMARY KEY,
id_troca INT NOT NULL UNIQUE,
id_usuario_avaliador INT NOT NULL,
nota INT NOT NULL,
comentario TEXT,
data_feedback DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (id_troca) REFERENCES trocas(id),
FOREIGN KEY (id_usuario_avaliador) REFERENCES usuarios(id)
);
</details>
