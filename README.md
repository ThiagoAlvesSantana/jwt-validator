# JWT Validator

Esta aplicação é uma API REST construída com Spring Boot que valida JSON Web Tokens (JWT) de acordo com critérios específicos. A validação garante que o JWT:

- Seja composto por exatamente três partes (header, payload e signature).
- Possua exatamente três claims: **Name**, **Role** e **Seed**.
- Tenha a claim **Name** sem caracteres numéricos e com tamanho máximo de 256 caracteres.
- Tenha a claim **Role** com um dos valores: **Admin**, **Member** ou **External**.
- Tenha a claim **Seed** representando um número primo.

A API retorna um valor booleano (`true` ou `false`) indicando se o JWT atende a todos os critérios de validação.

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.0-SNAPSHOT**
- **Maven** (gerenciamento de dependências e build)
- **SLF4J** (para logging e observabilidade)
- **Jackson** (para manipulação de JSON)
- **JUnit 5** (para testes unitários e de integração)

---

## Estrutura do Projeto

O projeto está organizado com foco em baixa acoplamento e alta coesão, seguindo os princípios SOLID. As principais camadas são:

- **Controller:**  
  `JwtValidatorController` expõe o endpoint `/api/validate` para receber o JWT via POST e retornar a validação.
  
- **Service:**  
  `JwtValidatorService` contém a lógica de validação do token, dividindo-o em partes e validando cada claim conforme os critérios definidos.
  
- **Utilitário:**  
  `PrimeUtils` oferece uma função para verificar se um número é primo.
  
- **Exceção Customizada:**  
  `JwtValidationException` centraliza e padroniza as exceções relacionadas à validação do JWT, facilitando o tratamento e a rastreabilidade dos erros.
  
- **Testes:**  
  Testes unitários foram implementados utilizando JUnit 5 para garantir que cada cenário de validação seja corretamente coberto.

---

## Instruções de Execução

### Pré-Requisitos

- **Java 17** instalado.
- **Maven** instalado.

### Build e Execução
mvn clean install

mvn spring-boot:run

A aplicação iniciará na porta 8080 (ou conforme configurado no arquivo application.properties


# Testando a API
## A API expõe o endpoint:

POST http://localhost:8080/api/validate
Você pode testar a API utilizando os comandos curl abaixo:

### Caso 1 – JWT Válido
curl -X POST http://localhost:8080/api/validate \
  -H "Content-Type: text/plain" \
  -d "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg"

### Caso 2 – JWT Inválido (Token malformado)
curl -X POST http://localhost:8080/api/validate \
  -H "Content-Type: text/plain" \
  -d "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg"

### Caso 3 – JWT Inválido (Claim Name com dígitos numéricos)
curl -X POST http://localhost:8080/api/validate \
  -H "Content-Type: text/plain" \
  -d "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs"


### Caso 4 – JWT Inválido (Claims extras presentes)
curl -X POST http://localhost:8080/api/validate \
  -H "Content-Type: text/plain" \
  -d "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY"

## Detalhes dos Métodos e Decisões de Projeto

# Métodos Principais
JwtValidatorService.validate(String jwt)

# Descrição:
Divide o token JWT em três partes, decodifica o payload e valida se contém as claims necessárias e se os valores atendem aos critérios definidos.

# Validações:
Verifica se o JWT é nulo ou vazio.
Garante que o token possua exatamente três partes.
Valida que existam apenas três claims: Name, Role e Seed.
Assegura que Name não contenha dígitos e tenha tamanho máximo de 256 caracteres.
Confirma que Role seja um dos valores permitidos (Admin, Member ou External).
Converte Seed para inteiro e verifica se é um número primo através do utilitário PrimeUtils.

# Tratamento de Exceções:
Caso qualquer validação falhe, a exceção customizada JwtValidationException é lançada, centralizando o tratamento de erros e facilitando a depuração.
JwtValidationException

# Descrição:
Classe customizada que centraliza as exceções ocorridas durante a validação do JWT.

# Decisão:
Utilizar exceções não verificadas (unchecked) simplifica o tratamento dos erros sem a necessidade de declarações explícitas em todos os métodos.
JwtValidatorController.validateJwt(String jwt)

# Descrição:
Endpoint REST que consome o JWT, invoca o serviço de validação e, em caso de exceção, retorna false para manter a interface da API conforme a especificação.

# Premissas e Decisões Adotadas
Assunção sobre o JWT:
A verificação da assinatura do JWT não foi implementada, pois o foco está na validação do formato e das claims conforme os critérios fornecidos.

# Centralização das Exceções:
A classe JwtValidationException foi criada para padronizar e centralizar o tratamento de erros, o que facilita o monitoramento e a manutenção da aplicação.

# Observabilidade:
A utilização do SLF4J para logging em cada etapa crítica permite uma melhor rastreabilidade e facilita a identificação de problemas durante a execução e em produção.

# Testabilidade e Manutenibilidade:
A estrutura modular, com clara separação de responsabilidades, permite a realização de testes unitários e de integração de forma eficiente.