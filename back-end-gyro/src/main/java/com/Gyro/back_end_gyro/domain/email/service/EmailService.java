package com.Gyro.back_end_gyro.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(buildTemplate(content), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }


    private String buildTemplate(String content) {
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
                            <a href="https://gyro.com" style="background-color: #FFA500; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block;">Acessar</a>
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
    """.formatted(content);
    }

}
