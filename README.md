Serviços iniciados para o Auth:

PostgreSQL: Rodando em localhost:5430 (Database: trinity-auth-service).

RabbitMQ: Painel administrativo em http://localhost:15672 (guest/guest).

2. Configurações do Projeto
   As configurações principais residem no src/main/resources/application.properties. Certifique-se de que as credenciais do banco coincidem com o seu ambiente Docker.

🔌 API Endpoints
Swagger UI: Com o projeto rodando, acesse  para a documentação técnica detalhada.

🏗️ Estrutura de Eventos
Ao realizar um cadastro bem-sucedido, o serviço publica eventos para as filas do RabbitMQ:

UserCreatedEvent

AcademicCreatedEvent

Alunocreatedevent

Esses eventos permitem que o microsserviço Core crie automaticamente as entidades de negócio relacionadas ao novo usuário.

⚠️ Resolução de Problemas (Troubleshooting)
Falha na Inicialização: Verifique se o container trinity-auth-db está ativo. O Spring Boot não iniciará se não conseguir conectar ao banco na porta 5430.

Erro 403 Forbidden: Certifique-se de estar enviando o Token JWT corretamente no Header (Authorization: Bearer <token>) para rotas protegidas.

Mensagens não chegam ao Core: Verifique no painel do RabbitMQ se as filas foram criadas e se há consumidores ativos.

Desenvolvido para o ecossistema Trinity Academy 🥋