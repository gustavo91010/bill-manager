# controle-de-pagamentos-3.0

## Uma evolução do projeto ControleDePagamentos_2.0


- Utilizei a interface JpaRepository do pacote pringframework.data.jpa
 para fazer a manipulação do bandco de dados;
 
-  adicionei a dependencia spring-boot-devtools do org.springframework.boot para fazer as alterações no projeto
 sem ter que encerara a aplicação a cada atualização;
 
 - Estou utilizando o banco de dados MySql para a produção e o bando em memoria H2 para os testes;
 
 - Para a separação dos bancod de dados, criei o perfil dev para  a produção, e o perfil test para os testes de integração;
 
 - Estou utilizando o Swagger para a documentação;
