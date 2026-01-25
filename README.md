# Gerência de Software - Sistema de Cafeteria

[![SonarQube](https://github.com/murilo-rossi/gerencia-software/actions/workflows/build.yml/badge.svg)](https://github.com/murilo-rossi/gerencia-software/actions/workflows/build.yml)

## Sobre o Projeto

Este projeto foi desenvolvido para a disciplina **SSC0535 - Gerência de Configuração, Manutenção e Evolução de Software** (2025) no Instituto de Ciências Matemáticas e de Computação (ICMC-USP).

O objetivo principal foi aplicar conceitos de gerência de configuração, análise de qualidade de código e integração contínua utilizando **GitHub Actions** para CI/CD e **SonarCloud** para análise estática de código.

O sistema em si é uma aplicação Java simples que simula o gerenciamento de uma cafeteria, com interfaces distintas para clientes e gerentes. Por ser um projeto acadêmico inicial (originalmente do 2º semestre da graduação), o código foi intencionalmente mantido "amador" para possibilitar a identificação e correção de problemas de qualidade.

---

## Funcionalidades

### Interface do Gerente
- **Gerenciamento de Estoque**
  - Adicionar novos produtos (nome, preço, quantidade)
  - Atualizar informações de produtos existentes
  - Excluir produtos do estoque
- **Relatórios de Vendas**
  - Visualizar histórico completo de pedidos
  - Informações detalhadas: data, produtos, quantidades e valores
- **Autenticação**
  - Acesso protegido por senha
  - Interface de mudança de senha (implementada posteriormente)

### Interface do Cliente
- **Catálogo de Produtos**
  - Visualização de produtos disponíveis
  - Informações de preço e disponibilidade
- **Sistema de Pedidos**
  - Adicionar produtos ao pedido
  - Remover itens do carrinho
  - Cálculo automático do valor total
  - Finalização de pedido

## Estrutura do Projeto

```
gerencia-software/
├── .github/
│   └── workflows/
│       └── build.yml          # GitHub Actions - CI/CD
├── src/
│   ├── Dados/                 # Arquivos de dados (.txt)
│   ├── main/                  # Classes principais
│   ├── telas/                 # Interfaces gráficas
│   └── paths/                 # Configurações de caminhos
├── pom.xml                    # Configuração Maven
└── sonar-project.properties   # Configuração SonarCloud
```

## Integração com SonarCloud

O projeto utiliza **SonarCloud** para análise contínua de qualidade de código. A cada push na branch `main`, o GitHub Actions compila o código com Maven, executa análise estática com SonarQube e envia os resultados ao SonarCloud.

### Métricas Analisadas
- **Security**: Vulnerabilidades de segurança
- **Reliability**: Bugs e code smells
- **Maintainability**: Duplicação e complexidade ciclomática
- **Coverage**: Cobertura de testes (alvo de melhoria)

### Visualizar Resultados
Acesse o dashboard do projeto no SonarCloud:
[https://sonarcloud.io/project/overview?id=murilo-rossi_gerencia-software](https://sonarcloud.io/project/overview?id=murilo-rossi_gerencia-software)

---

## Processo de Desenvolvimento

### Política de Versionamento
Utilizamos uma estratégia baseada em **Git Flow**:

- **`main`**: Versão estável de produção
- **`dev`**: Branch de desenvolvimento (integração de features)
- **`bug-sonarqube`**: Correções de problemas identificados pelo SonarCloud

### Plano de Mudanças

Durante o projeto, foram identificados **41 problemas** categorizados por prioridade:

| Prioridade | Tipo | Quantidade |
|-----------|------|------------|
| Alta | Corretiva | 8 |
| Média | Corretiva | 16 |
| Baixa | Corretiva | 15 |
| Evolutiva | Preventiva/Adaptativa | 2 |

### Principais Correções Realizadas

**Problemas Críticos (Alta Severidade)**
1. Serialização de coleções (`Estoque.produtos`, `Ordem.itens`)
2. Acesso estático a constantes (`DISPOSE_ON_CLOSE`)
3. Eliminação de duplicação de código (strings repetidas)
4. Inconsistências entre nomes de pacotes e caminhos

**Melhorias de Manutenibilidade**
- Substituição de `System.out` por loggers
- Refatoração de classes anônimas para lambdas
- Otimização de iterações (`entrySet` vs `keySet`)
- Remoção de variáveis não utilizadas
- Padronização de nomenclatura

**Funcionalidades Adicionadas**
- Sincronização em tempo real entre telas (cliente/gerente)
- Mudança de senha via interface gráfica

---

## Aprendizados

Este projeto proporcionou experiência prática em:

1. **Análise de Qualidade de Código**
   - Interpretação de métricas (code smells, duplicação, complexidade)
   - Priorização de refatorações baseada em severidade

2. **Integração Contínua (CI/CD)**
   - Configuração de GitHub Actions
   - Automação de builds e análises

3. **Ferramentas de Análise Estática**
   - SonarCloud/SonarQube
   - Compreensão de "atributos de código limpo" (consistência, intencionalidade, adaptabilidade)

4. **Gerência de Configuração**
   - Versionamento
   - Planejamento de mudanças

---