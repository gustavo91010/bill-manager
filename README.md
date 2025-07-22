Bill Manager API

API para gerenciamento de contas e despesas, desenvolvida em Java com Spring Boot.
Permite cadastrar, listar e gerenciar despesas pessoais ou empresariais, integrando-se com serviços externos e com um frontend Next.js.

✨ Funcionalidades

    📦 Registro de contas via AWS SQS

    🔐 Integração com serviço externo de autenticação

    👥 Cadastro livre de usuários

    💳 Gerenciamento de categorias e pagamentos

    🗂 API REST para comunicação com o frontend

🛠️ Tecnologias

    Java 11

    Spring Boot

    Spring Data JPA

    AWS SQS

    Flyway para migrations

    OpenFeign para clientes HTTP

    Swagger para documentação

    PostgreSQL

🚀 Como rodar
Pré-requisitos

    Java 11

    Maven 3.x

    PostgreSQL rodando

    (Opcional) Docker Compose

Executando localmente

bash

# Clone o repositório
git clone https://github.com/seu-usuario/bill-manager.git
cd bill-manager

# Build do projeto
./mvnw clean package

# Execute a aplicação
java -jar target/bill-manager-0.0.1-SNAPSHOT.jar

A API já está disponível publicamente em:
🌐 http://3.229.225.73:3000/
📅 Próximas funcionalidades

    📂 Organização das despesas por categoria

    🔎 Filtros e relatórios mais detalhados

    📈 Dashboard com novos gráficos

    🔐 Login via redirecionamento com OAuth2 (GitHub e Google)

👨‍💻 Autor

    Gustavo Paes

📄 Licença

Este projeto está licenciado sob a MIT License.
🔗 Links úteis

    Frontend: Bill Manager App

    Serviço de Autenticação: authentication-ms

    Documentação Swagger: /swagger-ui.html
