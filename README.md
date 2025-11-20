# Projeto Limpaí - Backend 

Este projeto é a API REST principal do projeto Limpaí 

---

## 🛠️ 1. Pré-requisitos

Para rodar a aplicação localmente, você precisa ter:

* **Java 21** ou mais recente (JDK).
* **Maven** (instalado localmente ou via Wrapper).
* **Docker** e **Docker Compose** instalados e rodando.
* **IDE** com suporte a Spring Boot/Maven (IntelliJ IDEA é recomendado).

---

## ⚙️ 2. Setup Completo (Build e Ambiente)

### 2.1. Clonagem e Inicialização (Recomendado para Demo)

A forma mais rápida de garantir que tudo está na mesma rede e configurado é usar o Docker Compose para subir ambos os serviços juntos:

1.  Clone o repositório:
    ```bash
    git clone https://github.com/Luis-coelho30/limpai-api.git
    cd limpai-backend
    ```

2.  **Inicie o Banco de Dados e a Aplicação:**
    ```bash
    # Constrói a imagem e sobe os serviços (MySQL + Backend)
    docker-compose up -d --build
    ```

---

## 💻 3. Rodando Localmente (Via IDE - Para Debug)

Se você preferir rodar a aplicação diretamente na sua IDE (IntelliJ, Eclipse).

1.  **Abra o Projeto na IDE:**
    * Importe o projeto Maven na sua IDE.

2.  **Defina as variáveis de ambiente na configuração de run da sua IDE:**
    * Exemplo IntelliJ IDEA:
        * Clique na run atual (geralmente Current File)
          
          <img width="327" height="43" alt="image" src="https://github.com/user-attachments/assets/2ef212e3-65c2-4da6-b229-34634bcc2b3d" />
        * Selecione a opção Edit Configurations
        
          <img width="396" height="450" alt="image" src="https://github.com/user-attachments/assets/ca3ec7ca-0163-417c-8679-8d7f31669cd2" />
        * Clique em Add new run configuration
          
          <img width="801" height="656" alt="image" src="https://github.com/user-attachments/assets/7c2058bd-f650-40ba-b8f2-89eee6158821" />
        * Selecione Application
          
          <img width="801" height="656" alt="image" src="https://github.com/user-attachments/assets/8cc393ff-ec1f-49d1-b7f7-16a1a28b96c2" />
        * Dê um nome para sua configuração
          
          <img width="564" height="656" alt="image" src="https://github.com/user-attachments/assets/b11425f1-189f-4e6a-b7b6-08feb2c66a96" />
        * Configure a classe main do projeto: `ProjetoLimpaiApplication.java` (Basta digitar e selecionar a primeira opção)

          <img width="564" height="656" alt="image" src="https://github.com/user-attachments/assets/e2a60d3f-f801-4288-abb2-258f60c192ab" />
        * Cole o seguinte texto em Enviromental Variables:
          ```bash
            JWT_SECRET=DuTQJODkoQQBTroTMQGmx//IRMXPCa8juq7qmY/DxP4=;SPRING_PROFILES_ACTIVE=test
          ```
          <img width="564" height="656" alt="image" src="https://github.com/user-attachments/assets/390482b2-2900-4c29-855e-b805404e70ee" />
        * Clique em Apply e Ok

3.  **Inicie o Backend:**
    * Inicie o projeto com o botão da IDE e a Run customizada selecionada.

---

## 🧪 4. Testando a Autenticação

**Certifique-se que você tenha o Postman baixado e pronto com um workspace vazio**

* **Importe as collections:** `Clique no botão Import e selecione a pasta postman-limpai`
* **Selecione o environment do pacote importado:** `Clique em "No environment", selecione "limpai-api-env"`
* **Configure a variável de ambiente base_url:** `Clique em "Environments" e defina o valor da variável para URL Base`
* **URL Base:** `http://localhost:8080`

---

## 🗑️ 5. Limpeza

Para garantir que você não gaste recursos em background se estiver em containers, use:

```bash
# Derruba todos os serviços
docker-compose down
# Para limpar os dados do MySQL (remover o volume)
# docker-compose down -v
