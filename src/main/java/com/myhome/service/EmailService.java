package com.myhome.service;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.myhome.singleton.ConfigurationManager;

/**
 * Serviço de envio de emails real usando JavaMail API.
 * Configurado para usar Gmail SMTP com autenticação via App Password.
 */
public class EmailService {

    private final String smtpServidor;
    private final String smtpPorta;
    private final String usuario;
    private final String senha;
    private final String remetente;
    private final boolean starttlsEnabled;
    private final boolean debug;

    /**
     * Construtor que carrega as configurações do ConfigurationManager.
     */
    public EmailService() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        this.smtpServidor = config.getProperty("servico.email.smtp.servidor", "smtp.gmail.com");
        this.smtpPorta = config.getProperty("servico.email.smtp.porta", "587");
        this.usuario = config.getProperty("servico.email.usuario", "");
        this.senha = config.getProperty("servico.email.senha", "");
        this.remetente = config.getProperty("servico.email.remetente", usuario);
        this.starttlsEnabled = Boolean.parseBoolean(
                config.getProperty("servico.email.starttls.enabled", "true"));
        this.debug = Boolean.parseBoolean(config.getProperty("sistema.debug", "false"));
    }

    /**
     * Envia um email para o endereço especificado com a mensagem fornecida.
     *
     * @param destinatario o endereço de email do destinatário
     * @param mensagem     a mensagem a ser enviada
     */
    public void enviar(String destinatario, String mensagem) {
        enviar(destinatario, "MyHome - Notificação", mensagem);
    }

    /**
     * Envia um email com assunto personalizado.
     *
     * @param destinatario o endereço de email do destinatário
     * @param assunto      o assunto do email
     * @param mensagem     a mensagem a ser enviada
     */
    public void enviar(String destinatario, String assunto, String mensagem) {
        System.out.println("[EMAIL] Preparando envio de email para: " + destinatario);

        // Validação básica
        if (destinatario == null || destinatario.trim().isEmpty()) {
            System.out.println("[EMAIL] Erro: Destinatário inválido.");
            return;
        }

        if (usuario == null || usuario.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            System.out.println("[EMAIL] Erro: Credenciais de email não configuradas.");
            System.out.println("[EMAIL] Configure servico.email.usuario e servico.email.senha no application.properties");
            return;
        }

        // Configurar propriedades SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(starttlsEnabled));
        props.put("mail.smtp.host", smtpServidor);
        props.put("mail.smtp.port", smtpPorta);
        props.put("mail.smtp.ssl.trust", smtpServidor);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Criar sessão com autenticação
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, senha);
            }
        });

        // Debug desativado para não poluir o terminal
        session.setDebug(false);

        try {
            // Criar mensagem
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente, "MyHome Imóveis"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            
            // Criar conteúdo HTML
            String htmlContent = criarConteudoHTML(mensagem);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            // Enviar email
            Transport.send(message);

            System.out.println("[EMAIL] ✓ Email enviado com sucesso para: " + destinatario);

        } catch (AuthenticationFailedException e) {
            System.out.println("[EMAIL] ✗ Erro de autenticação: Verifique usuário e senha.");
            System.out.println("[EMAIL] Certifique-se de usar uma 'Senha de App' do Gmail.");
            if (debug) {
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            System.out.println("[EMAIL] ✗ Erro ao enviar email: " + e.getMessage());
            if (debug) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("[EMAIL] ✗ Erro inesperado: " + e.getMessage());
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cria o conteúdo HTML do email com formatação profissional.
     *
     * @param mensagem a mensagem de texto
     * @return o conteúdo HTML formatado
     */
    private String criarConteudoHTML(String mensagem) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); ");
        html.append("color: white; padding: 20px; text-align: center; border-radius: 10px 10px 0 0; }");
        html.append(".header h1 { margin: 0; font-size: 24px; }");
        html.append(".content { background: #f9f9f9; padding: 30px; border: 1px solid #ddd; }");
        html.append(".message { background: white; padding: 20px; border-radius: 5px; ");
        html.append("border-left: 4px solid #667eea; margin: 20px 0; }");
        html.append(".footer { background: #333; color: #aaa; padding: 15px; text-align: center;");
        html.append("font-size: 12px; border-radius: 0 0 10px 10px; }");
        html.append(".footer a { color: #667eea; text-decoration: none; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class=\"container\">");
        html.append("<div class=\"header\">");
        html.append("<h1>🏠 MyHome Imóveis</h1>");
        html.append("<p>Seu portal de classificados imobiliários</p>");
        html.append("</div>");
        html.append("<div class=\"content\">");
        html.append("<div class=\"message\">");
        html.append(mensagem.replace("\n", "<br>"));
        html.append("</div>");
        html.append("</div>");
        html.append("<div class=\"footer\">");
        html.append("<p>Este é um email automático do sistema MyHome.</p>");
        html.append("<p>© 2026 MyHome Imóveis - Todos os direitos reservados</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }

    /**
     * Envia um email de teste para verificar as configurações.
     *
     * @param destinatario o endereço de email para teste
     * @return true se o email foi enviado com sucesso
     */
    public boolean enviarEmailTeste(String destinatario) {
        System.out.println("\n========================================");
        System.out.println("   TESTE DE ENVIO DE EMAIL - MyHome");
        System.out.println("========================================");
        System.out.println("Servidor SMTP: " + smtpServidor);
        System.out.println("Porta: " + smtpPorta);
        System.out.println("Usuário: " + usuario);
        System.out.println("Remetente: " + remetente);
        System.out.println("STARTTLS: " + starttlsEnabled);
        System.out.println("----------------------------------------");

        try {
            enviar(destinatario, "MyHome - Teste de Email",
                    "<h2>✅ Teste de Email Realizado com Sucesso!</h2>" +
                    "<p>Parabéns! Se você está lendo esta mensagem, o serviço de email do MyHome está funcionando corretamente.</p>" +
                    "<p><strong>Data do teste:</strong> " + java.time.LocalDateTime.now() + "</p>" +
                    "<p>Você pode agora receber notificações sobre:</p>" +
                    "<ul>" +
                    "<li>Novos anúncios de imóveis</li>" +
                    "<li>Atualizações de preço</li>" +
                    "<li>Mensagens de interessados</li>" +
                    "<li>Status de moderação</li>" +
                    "</ul>");
            return true;
        } catch (Exception e) {
            System.out.println("[EMAIL] Teste falhou: " + e.getMessage());
            return false;
        }
    }
}
