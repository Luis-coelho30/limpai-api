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
    docker-compose up --build
    ```

### 2.2. O Segredo das Variáveis

As variáveis críticas de ambiente (`JWT_SECRET`, `DB_USER/PASS`, etc.) **são injetadas pelo `docker-compose.yml`** quando a aplicação é executada via Docker, garantindo que o `SPRING_DATASOURCE_URL` aponte corretamente para o host `mysql-dev`.

---

## 💻 3. Rodando Localmente (Via IDE - Para Debug)

Se você preferir rodar a aplicação diretamente na sua IDE (IntelliJ, Eclipse).

1.  **Abra o Projeto na IDE:**
    * Importe o projeto Maven na sua IDE.

2.  **Defina uma chave Base64 válida no arquivo application-test.properties na variável JWT_SECRET**

2.  **Inicie o Backend (Método principal):**
    * Abra a classe principal (`ProjetoLimpaiApplication.java`).
    * Clique com o botão direito e selecione **Run** ou **Debug**.

---

## 🧪 4. Testando a Autenticação

* **URL Base:** `http://localhost:8080`
* **Login:** `POST /auth/login` (Obtém `accessToken` no corpo e `refreshToken` no cookie `HttpOnly`).
* **Uso:** Envie o `accessToken` como header: `Authorization: Bearer <token_aqui>`
* **Renovação:** `POST /auth/refresh` (Envia o cookie automaticamente).

---

## 🗑️ 5. Limpeza

Para garantir que você não gaste recursos em background se estiver em containers, use:

```bash
# Derruba todos os serviços
docker-compose down
# Para limpar os dados do MySQL (remover o volume)
# docker-compose down -v
