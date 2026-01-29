package com.myhome.facade;

import com.myhome.singleton.ConfigurationManager;
import java.util.List;

/**
 * RF07 - FACADE PATTERN
 * 
 * Implementação do padrão Facade para simplificar o acesso às configurações.
 * 
 * RESPONSABILIDADES:
 * - Fornecer interface simplificada para acessar configurações
 * - Ocultar a complexidade do ConfigurationManager
 * - Fornecer métodos específicos de domínio com nomes descritivos
 * 
 * BENEFÍCIOS DO FACADE:
 * 1. Simplificação: Interface mais simples e intuitiva
 * 2. Desacoplamento: Clientes não precisam conhecer detalhes internos
 * 3. Facilidade de uso: Métodos com nomes de negócio
 * 
 * INTEGRAÇÃO COM SINGLETON:
 * Esta Facade utiliza o ConfigurationManager (Singleton) internamente,
 * demonstrando como padrões podem trabalhar em conjunto.
 */
public class ConfigFacade {
    
    // ========================================
    // ATRIBUTOS
    // ========================================
    
    /**
     * Referência ao Singleton ConfigurationManager.
     * 
     * PADRÃO FACADE: A facade mantém referência aos subsistemas
     * que ela simplifica (neste caso, o ConfigurationManager).
     */
    private final ConfigurationManager configManager;
    
    // ========================================
    // CONSTRUTOR
    // ========================================
    
    /**
     * Construtor da Facade.
     * 
     * Obtém a instância única do ConfigurationManager.
     * Demonstra a integração entre Facade e Singleton.
     */
    public ConfigFacade() {
        // Obtém a instância do Singleton
        this.configManager = ConfigurationManager.getInstance();
    }
    
    // ========================================
    // MÉTODOS DE NEGÓCIO - CONFIGURAÇÕES GERAIS
    // ========================================
    
    /**
     * Retorna a taxa de comissão cobrada pela plataforma.
     * 
     * REGRA DE NEGÓCIO:
     * Taxa cobrada sobre o valor do anúncio para manutenção da plataforma.
     * 
     * @return Taxa de comissão (ex: 0.05 para 5%)
     */
    public double getTaxaComissao() {
        return configManager.getPropertyAsDouble("sistema.taxa.comissao");
    }
    
    /**
     * Retorna o limite máximo de fotos por anúncio.
     * 
     * REGRA DE NEGÓCIO:
     * Quantidade máxima de fotos que podem ser anexadas a um anúncio.
     * 
     * @return Número máximo de fotos permitidas
     */
    public int getLimiteUploadFotos() {
        return configManager.getPropertyAsInt("sistema.limite.fotos");
    }
    
    /**
     * Retorna o tamanho máximo de arquivo de foto em MB.
     * 
     * @return Tamanho máximo em megabytes
     */
    public int getTamanhoMaximoFotoMB() {
        return configManager.getPropertyAsInt("sistema.tamanho.max.foto.mb");
    }
    
    // ========================================
    // MÉTODOS DE NEGÓCIO - MODERAÇÃO (RF03)
    // ========================================
    
    /**
     * Retorna a lista de termos proibidos para moderação.
     * 
     * REGRA DE NEGÓCIO (RF03 - Chain of Responsibility):
     * Estes termos são verificados pelo TermosProibidosHandler
     * durante a cadeia de moderação de anúncios.
     * 
     * @return Lista de palavras/frases proibidas
     */
    public List<String> getTermosProibidos() {
        return configManager.getTermosProibidos();
    }
    
    /**
     * Retorna o número mínimo de fotos obrigatórias.
     * 
     * REGRA DE NEGÓCIO (RF03):
     * Usado pelo FotoDescricaoHandler para validar anúncios.
     * 
     * @return Número mínimo de fotos
     */
    public int getMinimoFotosAnuncio() {
        return configManager.getPropertyAsInt("moderacao.minimo.fotos");
    }
    
    /**
     * Retorna o número mínimo de caracteres na descrição.
     * 
     * REGRA DE NEGÓCIO (RF03):
     * Usado pelo FotoDescricaoHandler para validar descrições de anúncios.
     * 
     * @return Número mínimo de caracteres
     */
    public int getMinimoCaracteresDescricao() {
        return configManager.getPropertyAsInt("moderacao.minimo.caracteres.descricao");
    }
    
    // ========================================
    // MÉTODOS DE NEGÓCIO - PREÇOS (RF03)
    // ========================================
    
    /**
     * Retorna o preço mínimo permitido para anúncios.
     * 
     * REGRA DE NEGÓCIO (RF03):
     * Usado pelo PrecoValidoHandler na validação de anúncios.
     * Evita anúncios com preços irrealistas ou suspeitos.
     * 
     * @return Preço mínimo em reais
     */
    public double getPrecoMinimoAnuncio() {
        return configManager.getPropertyAsDouble("moderacao.preco.minimo");
    }
    
    /**
     * Retorna o preço máximo permitido para anúncios.
     * 
     * REGRA DE NEGÓCIO (RF03):
     * Usado pelo PrecoValidoHandler na validação de anúncios.
     * Evita valores exorbitantes ou erros de digitação.
     * 
     * @return Preço máximo em reais
     */
    public double getPrecoMaximoAnuncio() {
        return configManager.getPropertyAsDouble("moderacao.preco.maximo");
    }
    
    // ========================================
    // MÉTODOS DE NEGÓCIO - SERVIÇOS EXTERNOS
    // ========================================
    
    /**
     * Retorna a URL do serviço de pagamento.
     * 
     * REGRA DE NEGÓCIO:
     * Integração com gateway de pagamento para planos premium.
     * 
     * @return URL do serviço
     */
    public String getUrlServicoPagamento() {
        return configManager.getProperty("servico.pagamento.url");
    }
    
    /**
     * Retorna a URL do servidor SMTP para envio de e-mails.
     * 
     * REGRA DE NEGÓCIO (RF05 - Strategy):
     * Usado pela EmailNotificacao para configurar o servidor de e-mail.
     * 
     * @return URL do servidor SMTP
     */
    public String getUrlServicoEmail() {
        return configManager.getProperty("servico.email.smtp.servidor");
    }
    
    /**
     * Retorna a porta do servidor SMTP.
     * 
     * @return Porta SMTP
     */
    public int getPortaServicoEmail() {
        return configManager.getPropertyAsInt("servico.email.smtp.porta");
    }
    
    /**
     * Retorna o e-mail remetente padrão.
     * 
     * @return E-mail remetente
     */
    public String getEmailRemetente() {
        return configManager.getProperty("servico.email.remetente");
    }
    
    // ========================================
    // MÉTODOS DE NEGÓCIO - NOTIFICAÇÕES (RF05)
    // ========================================
    
    /**
     * Retorna a chave da API do serviço de SMS.
     * 
     * REGRA DE NEGÓCIO (RF05 - Strategy):
     * Usado pela SMSNotificacao para autenticação no provedor.
     * 
     * @return API Key do serviço de SMS
     */
    public String getApiKeySMS() {
        return configManager.getProperty("servico.sms.api.key");
    }
    
    /**
     * Retorna o provedor de SMS configurado.
     * 
     * @return Nome do provedor (ex: "Twilio", "Nexmo")
     */
    public String getProvedorSMS() {
        return configManager.getProperty("servico.sms.provedor");
    }
    
    /**
     * Retorna o token do bot do Telegram.
     * 
     * REGRA DE NEGÓCIO (RF05 - Strategy):
     * Usado pela TelegramNotification para autenticação.
     * 
     * @return Bot token do Telegram
     */
    public String getTelegramBotToken() {
        return configManager.getProperty("servico.telegram.bot.token");
    }
    
    /**
     * Retorna a API Key do WhatsApp Business.
     * 
     * REGRA DE NEGÓCIO (RF05 - Strategy):
     * Usado pela WhatsAppNotification para autenticação.
     * 
     * @return API Key do WhatsApp
     */
    public String getWhatsAppApiKey() {
        return configManager.getProperty("servico.whatsapp.api.key");
    }
    
    /**
     * Retorna o Business ID do WhatsApp.
     * 
     * @return Business ID
     */
    public String getWhatsAppBusinessId() {
        return configManager.getProperty("servico.whatsapp.business.id");
    }
    
    // ========================================
    // MÉTODOS DE NEGÓCIO - RELATÓRIOS (RF08)
    // ========================================
    
    /**
     * Retorna o diretório onde relatórios devem ser salvos.
     * 
     * REGRA DE NEGÓCIO (RF08 - Template Method):
     * Usado pelas classes de relatório (PDF, Excel, HTML) para
     * determinar onde salvar os arquivos gerados.
     * 
     * @return Caminho do diretório
     */
    public String getDiretorioRelatorios() {
        return configManager.getProperty("sistema.diretorio.relatorios", "relatorios/");
    }
    
    /**
     * Retorna o formato de data padrão para relatórios.
     * 
     * @return Padrão de formatação (ex: "dd/MM/yyyy")
     */
    public String getFormatoDataRelatorio() {
        return configManager.getProperty("sistema.formato.data", "dd/MM/yyyy");
    }
    
    // ========================================
    // MÉTODOS DE NEGÓCIO - DADOS CSV (E1)
    // ========================================
    
    /**
     * Retorna o caminho do arquivo CSV de imóveis.
     * 
     * REGRA DE NEGÓCIO (E1):
     * Arquivo usado para povoamento inicial de dados.
     * 
     * @return Caminho do arquivo
     */
    public String getCaminhoCSVImoveis() {
        return configManager.getProperty("dados.csv.imoveis", "data/imoveis.csv");
    }
    
    /**
     * Retorna o caminho do arquivo CSV de usuários.
     * 
     * @return Caminho do arquivo
     */
    public String getCaminhoCSVUsuarios() {
        return configManager.getProperty("dados.csv.usuarios", "data/usuarios.csv");
    }
    
    /**
     * Retorna o caminho do arquivo CSV de anúncios.
     * 
     * @return Caminho do arquivo
     */
    public String getCaminhoCSVAnuncios() {
        return configManager.getProperty("dados.csv.anuncios", "data/anuncios.csv");
    }
    
    // ========================================
    // MÉTODO AUXILIAR
    // ========================================
    
    /**
     * Verifica se o modo debug está ativado.
     * 
     * @return true se debug estiver ativado
     */
    public boolean isDebugMode() {
        return configManager.getPropertyAsBoolean("sistema.debug");
    }
}
