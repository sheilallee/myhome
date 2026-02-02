# MyHome - Plataforma de Classificados Imobiliários

## 📋 Informações do Projeto

| Item | Descrição |
|------|-----------|
| **Disciplina** | Padrões de Projeto de Software |
| **Professor** | Alex Sandro da Cunha Rêgo |
| **Período** | 2025.2 |

---

## 👥 Equipe 

- Jackson Ramos 
- Sheila Lee 
- Thiago Alexandre 

---

## 📖 Descrição da Solução

O **MyHome** é uma plataforma digital de classificados imobiliários que conecta proprietários, corretores, imobiliárias e potenciais compradores/locatários. O sistema permite que anunciantes publiquem anúncios detalhados de imóveis para venda ou aluguel, enquanto usuários comuns podem pesquisar, filtrar e visualizar esses anúncios.

### 🎯 Objetivos Principais

- ✅ Suportar diferentes tipos de imóveis (casas, apartamentos, terrenos, imóveis comerciais)
- ✅ Gerenciar múltiplos tipos de anúncios (venda, aluguel, temporada)
- ✅ Controlar diversos perfis de usuários com permissões distintas
- ✅ Permitir expansão flexível para novos tipos de imóveis e serviços
- ✅ Gerenciar diferentes formatos de pagamento e planos de assinatura
- ✅ Prover mecanismos de notificação através de múltiplos canais
- ✅ Processar buscas com múltiplos filtros combinados

### 🏗️ Arquitetura do Sistema

O sistema será desenvolvido seguindo os princípios de **Clean Architecture** e **SOLID**, utilizando padrões de projeto (Design Patterns) para garantir:

- **Extensibilidade**: Fácil adição de novos recursos sem modificar código existente
- **Manutenibilidade**: Código organizado, testável e fácil de entender
- **Reutilização**: Componentes desacoplados que podem ser reutilizados
- **Flexibilidade**: Adaptação a mudanças de requisitos com baixo impacto

---

## 🎨 Padrões de Projeto Utilizados

O MyHome implementa **padrões de projeto** estrategicamente distribuídos para atender aos requisitos funcionais:

### 📊 Visão Geral dos Padrões

| Requisito | Padrões | Propósito |
|-----------|---------|-----------|
| **RF01** | Factory Method + Builder | Criação controlada de Anúncios e Imóveis personalizados |
| **RF02** | Prototype | Criação de Imóveis a partir de padrões predefinidos |
| **RF03** | Chain of Responsibility | Validação em cadeia de regras de moderação |
| **RF04** | State + Observer | Gerenciamento do ciclo de vida e notificações |
| **RF05** | Strategy | Múltiplos canais de notificação intercambiáveis |
| **RF06** | Decorator | Filtros dinâmicos para busca avançada |
| **RF07** | Singleton | Configuração centralizada global |
| **RF08** | Facade | Simplificação de acesso aos subsistemas |

---

## 🔍 Especificação Detalhada dos Requisitos

### RF01 - Criação de Anúncios e Imóveis

**🎯 Padrões:** Factory Method + Builder

**📝 Descrição:**
O sistema permite o cadastro de diferentes tipos de anúncios (Venda, Aluguel, Temporada) e imóveis complexos (Casa, Apartamento, Terreno, Sala Comercial) de forma controlada, garantindo que informações obrigatórias sejam coletadas corretamente.

**🛠️ Implementação:**

- **Factory Method Pattern**: Cria diferentes tipos de anúncios (`Venda`, `Aluguel`, `Temporada`) através de factories concretas, permitindo adicionar novos tipos sem modificar código existente. Cada factory encapsula a lógica de criação específica do tipo de anúncio.

- **Builder Pattern**: Constrói objetos `Imovel` complexos passo a passo, garantindo que todas as informações obrigatórias (tipo, área, endereço, características específicas) sejam fornecidas antes da criação. O Builder permite configurar atributos opcionais de forma fluente.

**📂 Classes Principais:**
- `AnuncioFactory` - Factory abstrata para criação de anúncios
- `VendaFactory`, `AluguelFactory`, `TemporadaFactory` - Factories concretas
- `ImovelBuilder` - Interface Builder para construção de imóveis
- `ImovelBuilderImpl` - Implementação concreta do Builder

**🔗 Localização:**
```
src/
├── factory/
│   ├── AnuncioFactory.java
│   ├── VendaFactory.java
│   ├── AluguelFactory.java
│   └── TemporadaFactory.java
└── builder/
    ├── ImovelBuilder.java
    └── ImovelBuilderImpl.java
```

---

### RF02 - Instâncias de Anúncios Padrão

**🎯 Padrão:** Prototype

**📝 Descrição:**
Certos tipos de anúncios iniciam com configuração padrão (ex: Apartamento com 2 quartos, 60m²). O sistema permite clonar esses protótipos para criar novos anúncios rapidamente.

**🛠️ Implementação:**

- **Prototype Pattern**: Permite clonar imóveis predefinidos, copiando todas as características padrão e permitindo customizações posteriores.

**📂 Classes Principais:**
- `ImovelPrototype` - Interface Prototype com método `clone()`
- `ApartamentoPadrao`, `CasaPadrao` - Protótipos concretos predefinidos
- `PrototypeRegistry` - Registro de protótipos disponíveis

**🔗 Localização:**
```
src/prototype/
├── ImovelPrototype.java
├── ApartamentoPadrao.java
├── CasaPadrao.java
└── PrototypeRegistry.java
```

---

### RF03 - Publicação e Moderação

**🎯 Padrão:** Chain of Responsibility

**📝 Descrição:**
Anúncios submetidos passam por moderação antes de se tornarem públicos. As validações incluem verificação de termos proibidos, preço condizente e presença de fotos/descrição.

**🛠️ Implementação:**

- **Chain of Responsibility**: Cria uma cadeia de validadores independentes que processam o anúncio sequencialmente. Cada validador pode aprovar, reprovar ou passar para o próximo.

**📂 Classes Principais:**
- `ModeracaoHandler` - Handler abstrato da cadeia
- `TermosProibidosHandler` - Valida termos inadequados
- `PrecoValidoHandler` - Valida se o preço é condizente
- `FotoDescricaoHandler` - Valida presença de foto/descrição

**🔗 Localização:**
```
src/chain/
├── ModeracaoHandler.java
├── TermosProibidosHandler.java
├── PrecoValidoHandler.java
└── FotoDescricaoHandler.java
```

---

### RF04 - Ciclo de Vida do Anúncio

**🎯 Padrões:** State + Observer

**📝 Descrição:**
Cada anúncio possui um ciclo de vida (Rascunho → Moderação → Ativo → Vendido/Suspenso). Mudanças de estado disparam notificações automáticas e logs.

**🛠️ Implementação:**

- **State Pattern**: Encapsula o comportamento de cada estado do anúncio, permitindo transições controladas.
  
- **Observer Pattern**: Notifica automaticamente anunciantes e sistema de log quando o estado do anúncio muda.

**📂 Classes Principais:**
- `AnuncioState` - Interface State
- `RascunhoState`, `ModeracaoState`, `AtivoState`, `VendidoState`, `SuspensoState` - Estados concretos
- `AnuncioContext` - Contexto que mantém o estado atual
- `AnuncioObserver` - Interface Observer
- `AnuncianteObserver`, `LogObserver` - Observers concretos

**🔗 Localização:**
```
src/
├── state/
│   ├── AnuncioState.java
│   ├── RascunhoState.java
│   ├── ModeracaoState.java
│   ├── AtivoState.java
│   ├── VendidoState.java
│   └── SuspensoState.java
└── observer/
    ├── AnuncioObserver.java
    ├── AnuncianteObserver.java
    └── LogObserver.java
```

---

### RF05 - Notificação de Usuários

**🎯 Padrão:** Strategy

**📝 Descrição:**
O sistema notifica usuários sobre eventos através de diferentes canais (Email, SMS, Telegram, WhatsApp) conforme preferência do usuário.

**🛠️ Implementação:**

- **Strategy Pattern**: Encapsula diferentes algoritmos de notificação, permitindo trocar o canal dinamicamente em tempo de execução.

**📂 Classes Principais:**
- `NotificacaoStrategy` - Interface Strategy
- `EmailNotificacao`, `SMSNotificacao`, `TelegramNotificacao`, `WhatsAppNotificacao` - Estratégias concretas
- `NotificadorContext` - Contexto que utiliza a estratégia

**🔗 Localização:**
```
src/strategy/
├── NotificacaoStrategy.java
├── EmailNotificacao.java
├── SMSNotificacao.java
├── TelegramNotificacao.java
└── WhatsAppNotificacao.java
```

---

### RF06 - Busca Avançada

**🎯 Padrão:** Decorator

**📝 Descrição:**
Usuários buscam imóveis aplicando múltiplos filtros combinados (preço, localização, área, quartos). Filtros podem ser adicionados dinamicamente.

**🛠️ Implementação:**

- **Decorator Pattern**: Adiciona responsabilidades (filtros) dinamicamente a objetos de busca, permitindo combinações flexíveis sem criar subclasses.

**📂 Classes Principais:**
- `BuscaImovel` - Componente base
- `FiltroDecorator` - Decorator abstrato
- `FiltroPreco`, `FiltroLocalizacao`, `FiltroArea`, `FiltroQuartos` - Decorators concretos

**🔗 Localização:**
```
src/decorator/
├── BuscaImovel.java
├── FiltroDecorator.java
├── FiltroPreco.java
├── FiltroLocalizacao.java
├── FiltroArea.java
└── FiltroQuartos.java
```

---

### RF07 - Configuração Centralizada

**🎯 Padrão:** Singleton

**📝 Descrição:**
O sistema carrega configurações globais (taxas, limites, termos proibidos, URLs de serviços) de arquivo `.properties` através de um ponto de acesso único e centralizado, garantindo consistência em toda a aplicação.

**🛠️ Implementação:**

- **Singleton Pattern**: Garante uma única instância de `ConfigurationManager` acessível globalmente através do método estático `getInstance()`. O construtor privado previne múltiplas instâncias, e a inicialização eager garante thread-safety.

**📂 Classes Principais:**
- `ConfigurationManager` - Singleton para gerenciar configurações
- `application.properties` - Arquivo de configuração

**🔗 Localização:**
```
src/
├── singleton/
│   └── ConfigurationManager.java
└── resources/
    └── application.properties
```

---

### RF08 - Simplificação de Acesso aos Subsistemas

**🎯 Padrão:** Facade

**📝 Descrição:**
O sistema fornece uma interface unificada e simplificada para operações complexas que envolvem múltiplos subsistemas (criação de imóveis, publicação de anúncios, moderação, busca e notificações).

**🛠️ Implementação:**

- **Facade Pattern**: A classe `MyHomeFacade` encapsula a complexidade de coordenar múltiplos subsistemas (Factory para anúncios, Builder para imóveis, Chain of Responsibility para moderação, Decorator para buscas, Strategy para notificações). Clientes interagem apenas com a Facade, que delega chamadas aos subsistemas apropriados.

**📂 Classes Principais:**
- `MyHomeFacade` - Facade principal do sistema
- Integra: `AnuncioFactory`, `ImovelBuilder`, `ModerationHandler`, `SearchEngine`, `NotificationManager`

**🔗 Localização:**
```
src/facade/
└── MyHomeFacade.java
```

---

## 🚀 Como Executar o Projeto

### 📋 Pré-requisitos

- **Java JDK 11 ou superior** (recomendado JDK 21)
- IDE (opcional: IntelliJ IDEA, Eclipse, VS Code)

> ⚠️ **Nota**: Não é necessário instalar Maven! O projeto usa **Maven Wrapper** que funciona automaticamente em qualquer sistema operacional.

### 🔧 Instalação e Execução

#### 🎯 Método Recomendado: Scripts Automatizados

##### 🐧 **Linux / Mac**

```bash
# 1. Clone o repositório
git clone https://github.com/sheilallee/myhome.git
cd myhome

# 2. Execute o script (compila e roda automaticamente)
./run.sh
```

##### 🪟 **Windows**

```cmd
# 1. Clone o repositório
git clone https://github.com/sheilallee/myhome.git
cd myhome

# 2. Execute o script (compila e roda automaticamente)
run.bat
```

Ou simplesmente **clique duas vezes** no arquivo `run.bat` no Windows Explorer!

---

#### 🔨 Método Alternativo: Maven Wrapper Manual

##### 🐧 **Linux / Mac**

```bash
# Compilar
./mvnw clean compile

# Executar
./mvnw exec:java -Dexec.mainClass="com.myhome.Main"
```

##### 🪟 **Windows**

```cmd
# Compilar
mvnw.cmd clean compile

# Executar
mvnw.cmd exec:java -Dexec.mainClass="com.myhome.Main"
```

---

#### 💡 Método IDE

1. Importe o projeto como **Maven project**
2. Localize a classe [Main.java](src/main/java/com/myhome/Main.java)
3. Execute com `Run` ou `Debug`

---

### 📊 Povoamento de Dados

O sistema popula dados automaticamente a partir de arquivos JSON:

- `data/anuncios.json` - Anúncios salvos persistentemente

Os anúncios criados durante a execução são salvos automaticamente e recarregados na próxima inicialização.

---

### ✨ O que acontece ao executar?

Quando você roda `./run.sh` (Linux/Mac) ou `run.bat` (Windows):

1. ✅ O Maven Wrapper baixa automaticamente o Maven (se necessário)
2. ✅ Compila todo o código-fonte
3. ✅ Configura o classpath com todas as dependências
4. ✅ Executa a aplicação MyHome
5. ✅ Carrega anúncios salvos anteriormente

---

### 🆘 Solução de Problemas

#### "Maven Wrapper não encontrado"
```bash
# Gere novamente o wrapper
mvn wrapper:wrapper
```

#### "Permissão negada" (Linux/Mac)
```bash
# Dê permissão de execução
chmod +x mvnw run.sh
```

#### Erro de compilação
```bash
# Limpe o cache e recompile
./mvnw clean install
```

---

## 📁 Estrutura do Projeto

```
myhome/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── myhome/
│   │   │           ├── factory/          # RF01: Factory Method
│   │   │           ├── builder/          # RF01: Builder Pattern
│   │   │           ├── prototype/        # RF02: Prototype
│   │   │           ├── chain/            # RF03: Chain of Responsibility
│   │   │           ├── state/            # RF04: State
│   │   │           ├── observer/         # RF04: Observer
│   │   │           ├── strategy/         # RF05: Strategy
│   │   │           ├── decorator/        # RF06: Decorator
│   │   │           ├── singleton/        # RF07: Singleton
│   │   │           ├── facade/           # RF08: Facade
│   │   │           ├── model/            # Entidades de domínio
│   │   │           ├── service/          # Serviços de negócio
│   │   │           ├── util/             # Utilitários
│   │   │           └── Main.java         # Classe principal
│   │   └── resources/
│   │       ├── application.properties    # Configurações
│   │       └── data/                     # Arquivos CSV
│   │           ├── imoveis.csv
│   │           ├── usuarios.csv
│   │           └── anuncios.csv
│   └── test/
│       └── java/
│           └── com/
│               └── myhome/              # Testes unitários
├── docs/
│   ├── diagrams/                        # Diagramas UML
│   │   ├── class-diagram.puml
│   │   └── architecture-diagram.puml
│   └── especificacao.pdf                # Documento de especificação
├── pom.xml                              # Configuração Maven
├── build.gradle                         # Configuração Gradle
└── README.md                            # Este arquivo
```

---

## 🧪 Testes

O projeto inclui testes unitários para todos os padrões implementados:

```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn test jacoco:report

# Ver relatório de cobertura
open target/site/jacoco/index.html
```

---

## 📚 Referências

- [Refactoring Guru - Design Patterns](https://refactoring.guru/pt-br/design-patterns/catalog)

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos na disciplina de Padrões de Projeto de Software.

