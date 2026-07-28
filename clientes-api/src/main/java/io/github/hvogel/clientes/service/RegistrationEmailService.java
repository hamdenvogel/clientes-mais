package io.github.hvogel.clientes.service;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class RegistrationEmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationEmailService.class);

  private final JavaMailSender mailSender;

  @Value("${app.registration.mail.from:noreply@clientes.local}")
  private String from;

  @Value("${app.registration.mail.from-name:Clientes}")
  private String fromName;

  @Value("${app.registration.mail.enabled:false}")
  private boolean emailEnabled;

  public RegistrationEmailService(ObjectProvider<JavaMailSender> mailSenderProvider) {
    this.mailSender = mailSenderProvider.getIfAvailable();
  }

  public void sendConfirmationEmail(String username, String to, String confirmationUrl, long expirationHours) {
    if (!emailEnabled) {
      LOGGER.info("[EMAIL DESATIVADO] Confirmacao para {}: {}", to, confirmationUrl);
      return;
    }

    if (mailSender == null) {
      throw new IllegalStateException(
          "Envio de e-mail habilitado, mas JavaMailSender nao esta configurado. Verifique spring.mail.* e o starter de mail.");
    }

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

      helper.setFrom(from, fromName);
      helper.setTo(to);
      helper.setSubject("Confirme seu cadastro - Plataforma Clientes");
      helper.setText(buildTemplate(username, confirmationUrl, expirationHours), true);

      mailSender.send(message);
    } catch (MessagingException | MailException ex) {
      throw new IllegalStateException("Nao foi possivel enviar o e-mail de confirmacao. Tente novamente.", ex);
    } catch (Exception ex) {
      throw new IllegalStateException("Erro ao preparar o e-mail de confirmacao.", ex);
    }
  }

  private String buildTemplate(String username, String confirmationUrl, long expirationHours) {
    return """
        <!doctype html>
        <html lang=\"pt-BR\">
          <head>
            <meta charset=\"UTF-8\" />
            <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />
            <title>Confirmacao de Cadastro</title>
          </head>
          <body style=\"margin:0;padding:0;background:#f1f5f9;font-family:Arial,sans-serif;color:#0f172a;\">
            <table width=\"100%%\" cellspacing=\"0\" cellpadding=\"0\" style=\"padding:32px 12px;\">
              <tr>
                <td align=\"center\">
                  <table width=\"620\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width:620px;background:#ffffff;border-radius:18px;overflow:hidden;border:1px solid #dbeafe;\">
                    <tr>
                      <td style=\"padding:28px 32px;background:linear-gradient(135deg,#0f172a,#1d4ed8);color:#ffffff;\">
                        <h1 style=\"margin:0;font-size:24px;\">Plataforma Clientes</h1>
                        <p style=\"margin:10px 0 0 0;opacity:.9;\">Confirmacao de cadastro e definicao de senha</p>
                      </td>
                    </tr>
                    <tr>
                      <td style=\"padding:30px 32px;\">
                        <p style=\"margin:0 0 14px 0;font-size:16px;\">Ola, <strong>%s</strong>!</p>
                        <p style=\"margin:0 0 18px 0;line-height:1.6;color:#334155;\">
                          Recebemos sua solicitacao de cadastro. Para concluir com seguranca,
                          confirme seu e-mail e defina sua senha por meio do botao abaixo.
                        </p>
                        <p style=\"margin:0 0 22px 0;line-height:1.6;color:#334155;\">
                          Este link expira em <strong>%d hora(s)</strong> e pode ser utilizado apenas uma vez.
                        </p>
                        <p style=\"margin:0 0 24px 0;\">
                          <a href=\"%s\" style=\"display:inline-block;padding:14px 22px;background:#2563eb;color:#ffffff;text-decoration:none;border-radius:10px;font-weight:700;\">Confirmar cadastro</a>
                        </p>
                        <p style=\"margin:0 0 10px 0;line-height:1.6;color:#334155;\">
                          Se o botao nao funcionar, copie e cole o link abaixo no navegador:
                        </p>
                        <p style=\"margin:0 0 24px 0;word-break:break-all;color:#1d4ed8;font-size:13px;\">%s</p>
                        <p style=\"margin:0;color:#64748b;font-size:13px;line-height:1.5;\">
                          Se voce nao reconhece esta solicitacao, ignore este e-mail com seguranca.
                        </p>
                      </td>
                    </tr>
                    <tr>
                      <td style=\"padding:18px 32px;background:#f8fafc;color:#64748b;font-size:12px;\">
                        Equipe Plataforma Clientes
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </body>
        </html>
        """.formatted(username, expirationHours, confirmationUrl, confirmationUrl);
  }
}

