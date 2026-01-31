# Fluxo do Sistema MyHome

## 📋 Visão Geral

O MyHome é um sistema de classificados imobiliários que demonstra a aplicação de 8 padrões de projeto (GoF) em um contexto real. O sistema opera através do **console Java** de forma interativa.

---

## 🔄 Fluxo Principal de Execução

### 1. **Inicialização do Sistema**

```
Main.java (RF07 + RF08)
    ↓
MyHomeFacade (RF08)
    ↓
ConfigurationManager.getInstance() (RF07)
    ↓
Carrega application.properties
```

**Padrões Aplicados:**
- **Singleton (RF07)**: Garante instância única de configuração
- **Facade (RF08)**: Interface simplificada para subsistemas complexos

---

### 2. **Criação de Imóvel** (RF01)

```
USUÁRIO
    ↓
[Escolhe tipo de imóvel]
    ↓
ImovelBuilder (RF01 - Builder)
    ├─→ setTipo("Casa")
    ├─→ setArea(150.0)
    ├─→ setEndereco("Rua X, 123")
    ├─→ setQuartos(3)
    └─→ build() → Imovel
```

**Padrão Builder:** Construção passo a passo de objetos `Imovel` complexos

**Fluxo Interativo:**
1. Sistema exibe: "Qual tipo de imóvel? (1-Casa, 2-Apartamento, 3-Terreno, 4-Sala Comercial)"
2. Usuário escolhe: `1`
3. Sistema solicita: "Área (m²):"
4. Usuário informa: `150.0`
5. Sistema solicita: "Endereço:"
6. Usuário informa: `Rua das Flores, 123`
7. Sistema solicita: "Número de quartos:"
8. Usuário informa: `3`
9. Builder constrói o objeto `Casa`

---

### 3. **Criação de Anúncio** (RF01)

```
IMOVEL CRIADO
    ↓
[Usuário escolhe tipo de anúncio]
    ↓
AnuncioFactory (RF01 - Factory Method)
    ├─→ VendaFactory.criarAnuncio(imovel, dados)
    ├─→ AluguelFactory.criarAnuncio(imovel, dados)
    └─→ TemporadaFactory.criarAnuncio(imovel, dados)
    ↓
ANUNCIO (Rascunho)
```

**Padrão Factory Method:** Criação de diferentes tipos de anúncios

**Fluxo Interativo:**
1. Sistema exibe: "Tipo de anúncio? (1-Venda, 2-Aluguel, 3-Temporada)"
2. Usuário escolhe: `1`
3. Sistema solicita: "Título do anúncio:"
4. Usuário informa: `Casa espaçosa 3 quartos`
5. Sistema solicita: "Preço:"
6. Usuário informa: `450000.00`
7. Sistema solicita: "Descrição:"
8. Usuário informa: `Casa ampla com quintal`
9. `VendaFactory` cria o anúncio
10. Anúncio inicia em estado **Rascunho**

---

### 4. **Uso de Protótipos** (RF02)

```
USUÁRIO QUER CRIAR ANÚNCIO PADRÃO
    ↓
PrototypeRegistry.getInstance() (RF02)
    ↓
getPrototipo("apartamento-padrao-2q")
    ↓
Imovel.clone()
    ↓
Customiza propriedades específicas
    ↓
IMOVEL PERSONALIZADO
```

**Padrão Prototype:** Clonagem de imóveis predefinidos

**Fluxo Interativo:**
1. Sistema exibe: "Deseja usar modelo padrão? (1-Sim, 2-Não)"
2. Usuário escolhe: `1`
3. Sistema lista protótipos disponíveis:
   - `1. Apartamento Padrão 2 Quartos (60m²)`
   - `2. Casa Padrão 3 Quartos (120m²)`
   - `3. Sala Comercial Padrão (45m²)`
4. Usuário escolhe: `1`
5. Sistema clona protótipo
6. Sistema permite customização: "Alterar endereço? (S/N)"
7. Usuário personaliza dados

---

### 5. **Publicação e Moderação** (RF03 + RF04)

```
ANUNCIO (Rascunho)
    ↓
[Usuário solicita publicação]
    ↓
AnuncioState: Rascunho.enviarParaModeracao()
    ↓
Estado: EM_MODERACAO (RF04 - State)
    ↓
ModerationHandler (RF03 - Chain of Responsibility)
    ├─→ TermsValidator → [Aprovado]
    ├─→ PriceValidator → [Aprovado]
    ├─→ PhotoValidator → [Aprovado]
    └─→ DescriptionValidator → [Aprovado]
    ↓
AnuncioState: EmModeracao.aprovar()
    ↓
Estado: ATIVO
    ↓
NotificationObserver (RF04 - Observer)
    ├─→ LogObserver.onEstadoAlterado()
    └─→ NotificationObserver.onEstadoAlterado()
```

**Padrões Aplicados:**
- **Chain of Responsibility (RF03)**: Validações em cadeia
- **State (RF04)**: Gerenciamento de estados do anúncio
- **Observer (RF04)**: Notificações automáticas

**Fluxo Interativo:**
1. Sistema exibe: "Anúncio salvo como RASCUNHO. Publicar? (S/N)"
2. Usuário confirma: `S`
3. Sistema: "Enviando para moderação..."
4. Validações executam:
   - ✓ Termos proibidos: OK
   - ✓ Preço válido: OK
   - ✓ Fotos presentes: OK
   - ✓ Descrição adequada: OK
5. Sistema: "Anúncio APROVADO e agora está ATIVO!"
6. Observadores são notificados:
   - Log registrado: `[2026-01-30 14:30] Anúncio #123 publicado`
   - Email enviado ao anunciante

---

### 6. **Notificação de Usuários** (RF05)

```
EVENTO (mudança de estado)
    ↓
NotificationManager (RF05 - Strategy)
    ↓
Usuario.getCanalNotificacao()
    ├─→ EmailNotification.enviar()
    ├─→ SMSNotification.enviar()
    ├─→ WhatsAppNotification.enviar()
    └─→ TelegramNotification.enviar()
```

**Padrão Strategy:** Algoritmos de notificação intercambiáveis

**Fluxo Interativo:**
1. Sistema detecta mudança de estado
2. Sistema verifica preferência do usuário
3. Se `canal = EMAIL`:
   - Envia: "Seu anúncio 'Casa espaçosa 3 quartos' foi aprovado!"
4. Se `canal = SMS`:
   - Envia: "Anúncio aprovado! Acesse: myhome.com/anuncio/123"
5. Usuário pode alterar canal a qualquer momento

---

### 7. **Busca Avançada** (RF06)

```
USUÁRIO BUSCA IMÓVEIS
    ↓
BuscaPadrao (RF06 - Decorator)
    ↓
[Adiciona filtros dinamicamente]
    ├─→ PrecoDecorator(min=200k, max=500k)
    ├─→ LocalizacaoDecorator(cidade="João Pessoa")
    ├─→ AreaDecorator(minima=100.0)
    ├─→ TipoImovelDecorator(tipo="Casa")
    └─→ EstadoAtivoDecorator()
    ↓
SearchEngine.buscar(filtros)
    ↓
LISTA DE ANUNCIOS FILTRADOS
```

**Padrão Decorator:** Filtros dinâmicos e combináveis

**Fluxo Interativo:**
1. Sistema exibe: "Digite filtros (ou Enter para buscar tudo):"
2. Usuário: "Adicionar filtro de preço? (S/N)"
3. Usuário escolhe: `S`
4. Sistema: "Preço mínimo:"
5. Usuário: `200000`
6. Sistema: "Preço máximo:"
7. Usuário: `500000`
8. Sistema: "Adicionar filtro de localização? (S/N)"
9. Usuário escolhe: `S`
10. Sistema: "Cidade:"
11. Usuário: `João Pessoa`
12. Sistema executa busca decorada
13. Resultado: `15 imóveis encontrados`

---

## 🎯 Casos de Uso Completos

### **Caso 1: Proprietário Publica Casa para Venda**

```
1. Sistema inicia
2. Usuário escolhe: "Criar novo anúncio"
3. Cria imóvel usando Builder:
   - Tipo: Casa
   - Área: 150m²
   - Endereço: Rua X, 123
   - Quartos: 3
   - Garagem: 2 vagas
4. Cria anúncio usando Factory:
   - Tipo: Venda
   - Preço: R$ 450.000,00
   - Título: "Casa 3 quartos com quintal"
   - Descrição: "Casa ampla..."
5. Sistema salva como RASCUNHO
6. Usuário solicita publicação
7. Chain of Responsibility valida:
   ✓ Termos OK
   ✓ Preço OK
   ✓ Fotos OK
   ✓ Descrição OK
8. Estado muda para ATIVO
9. Observer notifica usuário via Email
10. Anúncio disponível para busca
```

### **Caso 2: Comprador Busca Imóvel**

```
1. Sistema inicia
2. Usuário escolhe: "Buscar imóveis"
3. Adiciona filtros (Decorator):
   - Preço: R$ 300k - R$ 500k
   - Cidade: João Pessoa
   - Tipo: Casa
   - Área mínima: 100m²
4. SearchEngine aplica filtros encadeados
5. Sistema exibe: "12 imóveis encontrados"
6. Usuário visualiza lista
7. Usuário seleciona imóvel
8. Sistema exibe detalhes completos
9. Sistema registra visualização (log)
```

---

## 🖥️ Interface Console - Estrutura

### **Menu Principal**

```
╔════════════════════════════════════════╗
║      MYHOME - CLASSIFICADOS          ║
║        IMOBILIÁRIOS                   ║
╠════════════════════════════════════════╣
║  1. Criar novo anúncio                ║
║  2. Buscar imóveis                    ║
║  3. Meus anúncios                     ║
║  4. Configurações                     ║
║  5. Demonstrar padrões                ║
║  0. Sair                              ║
╚════════════════════════════════════════╝
Escolha uma opção:
```

### **Submenu: Criar Anúncio**

```
╔════════════════════════════════════════╗
║       CRIAR NOVO ANÚNCIO              ║
╠════════════════════════════════════════╣
║  1. Usar modelo padrão (Prototype)    ║
║  2. Criar do zero (Builder)           ║
║  0. Voltar                            ║
╚════════════════════════════════════════╝
Escolha uma opção:
```

---

## 📊 Diagrama de Estados (RF04)

```
┌─────────────┐
│  RASCUNHO   │ Estado inicial
└─────┬───────┘
      │ enviarParaModeracao()
      ↓
┌─────────────┐
│ EM_MODERACAO│ Validações (RF03)
└─────┬───────┘
      │ aprovar()        reprovar()
      ├────────┐            │
      ↓        │            ↓
┌─────────┐   │     ┌──────────┐
│  ATIVO  │   │     │REPROVADO │
└────┬────┘   │     └──────────┘
     │        │
     │ vender()│ suspender()
     ↓        ↓
┌─────────┐ ┌──────────┐
│ VENDIDO │ │SUSPENSO  │
└─────────┘ └──────────┘
```

---

## 🔗 Integração dos Padrões

```
FACADE (RF08)
    ├─→ coordena → FACTORY (RF01)
    ├─→ coordena → BUILDER (RF01)
    ├─→ coordena → CHAIN (RF03)
    ├─→ coordena → STATE (RF04)
    ├─→ coordena → OBSERVER (RF04)
    ├─→ coordena → STRATEGY (RF05)
    ├─→ coordena → DECORATOR (RF06)
    └─→ usa → SINGLETON (RF07)
```

**MyHomeFacade** é o ponto central que:
- Cria imóveis via Builder
- Cria anúncios via Factory
- Publica anúncios (State + Chain)
- Busca imóveis (Decorator)
- Notifica usuários (Strategy + Observer)
- Acessa configurações (Singleton)

---

## 💡 Observações de Implementação

### **Console Java**

✅ **Vantagens:**
- Simplicidade máxima
- Foco nos padrões de projeto
- Sem dependências externas
- Execução imediata
- Ideal para demonstração acadêmica

✅ **Implementação:**
```java
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MyHomeFacade facade = new MyHomeFacade();
        
        while (true) {
            exibirMenu();
            int opcao = scanner.nextInt();
            
            switch (opcao) {
                case 1: criarAnuncio(scanner, facade); break;
                case 2: buscarImoveis(scanner, facade); break;
                // ...
            }
        }
    }
}
```

### **Execução**

```bash
# Compilar
javac -d bin src/main/java/com/myhome/**/*.java

# Executar
java -cp bin com.myhome.Main
```

---

## 📝 Observações Finais

- O sistema demonstra **8 padrões GoF** integrados
- Interface **console interativa** com menus
- Validação de dados em tempo real
- Logs detalhados para rastreamento
- Configuração via arquivo `.properties`
- Arquitetura **SOLID** e **Clean Architecture**

---

**Autor:** MyHome Team  
**Versão:** 1.0  
**Data:** Janeiro 2026
