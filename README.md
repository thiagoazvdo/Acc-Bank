# Acc-Bank

## Rodando a Aplicação com Docker

Siga os passos abaixo para construir e executar a aplicação usando Docker.

### 1. Abrir um Terminal

Abra um terminal no diretório raiz do projeto. Este diretório deve conter o arquivo `docker-compose.yml` e o script `build.sh`.

### 2. Construir a imagem Docker da Aplicação

Execute o script `build.sh` para gerar a imagem Docker da aplicação.

```bash
bash build.sh
```

### 3. Iniciar a Aplicação

Inicie a aplicação em modo detached (em segundo plano) usando Docker Compose:

```bash
docker compose up -d
```

Isso iniciará os containers definidos no arquivo `docker-compose.yml` e permitirá que eles rodem em segundo plano. 

### 5. Parar e Remover a Aplicação

Quando você quiser parar e remover os containers, execute:

```bash
docker compose down
```
