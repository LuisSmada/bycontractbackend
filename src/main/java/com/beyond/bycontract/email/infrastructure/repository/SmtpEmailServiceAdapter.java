package com.beyond.bycontract.email.infrastructure.repository;

import com.beyond.bycontract.email.domain.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j // Génère un logger pour afficher les erreurs éventuelles
public class SmtpEmailServiceAdapter implements EmailRepository {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

//    @Async // Exécute l'envoi d'email dans un thread séparé
//    @Override
//    public void sendPasswordResetEmail(String email, String resetUrl) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("contact@groupebeyond.com");
//        message.setTo(email);
//        message.setSubject("Réinitialisation de votre mot de passe - BYCONTRACT");
//
//        String emailBody = """
//                Bonjour,
//
//                Vous avez demandé à réinitialiser votre mot de passe sur BYCONTRACT.
//                Veuillez cliquer sur le lien ci-dessous pour créer un nouveau mot de passe :
//
//                %s
//
//                Ce lien est valide pendant 15 minutes.
//                Si vous n'êtes pas à l'origine de cette demande, vous pouvez ignorer cet email.
//
//                L'équipe Groupe BEYOND
//                """.formatted(resetUrl);
//
//        message.setText(emailBody);
//        mailSender.send(message);
//    }

    @Async
    @Override
    public void sendPasswordResetEmail(String toEmail, String resetUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // Le "true" active le mode "multipart" (indispensable pour attacher des images)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("contact@groupebeyond.com");
            helper.setTo(toEmail);
            helper.setSubject("Réinitialisation de votre mot de passe - BYCONTRACT");

            // 1. Envoyer la variable resetUrl au template HTML
            Context context = new Context();
            context.setVariable("resetUrl", resetUrl);

            // 2. Générer le code HTML final (va chercher le fichier emails/reset-password.html)
            String htmlContent = templateEngine.process("emails/reset-password", context);

            // Le "true" dit à Spring que ce texte est du HTML
            helper.setText(htmlContent, true);

            // 3. Attacher le logo de manière invisible (Inline)
            // Le nom "logoImage" DOIT correspondre au "cid:logoImage" dans le HTML
            ClassPathResource logo = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", logo);

            // 4. Envoyer l'email
            mailSender.send(message);

            log.info("Email de réinitialisation envoyé avec succès à {}", toEmail);

        } catch (MessagingException e) {
            log.error("Échec de l'envoi de l'email à {}", toEmail, e);
            throw new RuntimeException("Erreur technique lors de l'envoi de l'email.");
        }
    }
}
