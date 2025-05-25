package com.Gyro.back_end_gyro.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${HTTPS_IP_ADDRESS}")
    private String gyroUrl;

    public void sendEmail(String to, String subject, String content) {
        log.info("[EmailService] Preparando para enviar email para: {}", to);
        log.debug("[EmailService] Assunto: {}", subject);
        log.debug("[EmailService] Conteúdo: {}", content);

        try {
            log.debug("[EmailService] Criando MimeMessage...");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            log.debug("[EmailService] Configurando destinatário e assunto...");
            helper.setTo(to);
            helper.setSubject(subject);

            log.debug("[EmailService] Construindo template HTML...");
            String emailContent = buildTemplate(content, this.gyroUrl);
            helper.setText(emailContent, true);

            log.info("[EmailService] Enviando email para {}...", to);
            mailSender.send(message);
            log.info("[EmailService] Email enviado com sucesso para {}", to);

        } catch (MessagingException e) {
            log.error("[EmailService] Erro ao enviar email para {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Erro ao enviar e-mail", e);
        } catch (Exception e) {
            log.error("[EmailService] Erro inesperado ao enviar email para {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Erro inesperado ao enviar e-mail", e);
        }
    }

    private String buildTemplate(String content, String ourSite) {
        log.debug("[EmailService] Construindo template HTML com conteúdo: {} e site: {}",
                content.length() > 50 ? content.substring(0, 50) + "..." : content,
                ourSite);

        return """
        <!DOCTYPE html>
        <html lang="pt-br">
        <head>
            <meta charset="UTF-8">
            <title>GYRO - Notificação</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
            <table align="center" width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: auto;">
                <tr>
                    <td style="background-color: #000000; padding: 20px; text-align: center;">
                        <h1 style="color: #FFA500; margin: 0;">GYRO</h1>
                    </td>
                </tr>
                <tr>
                    <td style="background-color: #ffffff; padding: 30px;">
                        <h2 style="color: #000000;">Olá!</h2>
                        <p style="color: #333333; font-size: 16px; line-height: 1.5;">%s</p>
                        <div style="margin-top: 30px; text-align: center;">
                            <a href="%s/login" style="background-color: #FFA500; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block;">Acessar</a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="background-color: #000000; padding: 15px; text-align: center;">
                        <p style="color: #ffffff; font-size: 14px; margin: 0;">&copy; 2025 GYRO. Todos os direitos reservados.</p>
                    </td>
                </tr>
            </table>
        </body>
        </html>
        """.formatted(content, ourSite);
    }
}