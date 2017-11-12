# Campanha Service

Aplicação REST utilizando Java 8, Spring Boot como framework base para a construção da aplicação, Spring Data JPA para acesso aos dados armazenados no banco de dados, banco de dados H2 em memória, Swagger para documentação da API e Maven como ferramenta de build e gerenciamento de dependências. Implementação do CRUD de campanhas solicitado.

# Observações
Ao cadastrar uma campanha, foi considerado que as outras campanha ativas no mesmo período são aquelas em que o ínicio ou o fim estão dentro da vigência da campanha nova.

## Para rodar a aplicação
Após clonar este repositório, ir até até o diretório criado e rodar o seguinte comando <code>mvn spring-boot:run</code>

## Endereço da aplicação
A aplicação foi configurada pra executar no seguinte endereço: http://localhost:8080

## Documentação
Como solução para documentar API foi utilizado o Swagger e disponibilizada no seguinte endereço: http://localhost:8080/swagger-ui.html

## Exemplos de chamadas

Listar campanhas ativas:

**GET /campanha**
Resposta:
```
[
    {
        "id": 1,
        "nome": "Black friday",
        "time": 1,
        "dataInicioVigencia": "2017-01-01",
        "dataFimVigencia": "2018-01-04"
    }
]
```

Cadastrar campanha:

**POST /campanha**
```
{
	"nome": "Black friday",
	"time": 1,
	"dataInicioVigencia": "2017-10-01",
	"dataFimVigencia": "2017-12-03"
}
```

Resposta:
```
{
    "id": 1,
    "nome": "Black friday",
    "time": 1,
    "dataInicioVigencia": "2017-10-01",
    "dataFimVigencia": "2017-12-03"
}
```

Atualizar campanha:

**PUT /campanha**
```
{
	"nome": "Black friday",
	"time": 1,
	"dataInicioVigencia": "2017-01-01",
	"dataFimVigencia": "2017-12-30"
}
```

Resposta:
```
{
    "id": 1,
    "nome": "Black friday",
    "time": 1,
    "dataInicioVigencia": "2017-01-01",
    "dataFimVigencia": "2017-12-30"
}
```


## Notificação de atualizações
Existe uma API que exibe as últimas 50 atualizações realizadas na API de campanha, disponibilizada no endereço http://localhost:8080/notificacao