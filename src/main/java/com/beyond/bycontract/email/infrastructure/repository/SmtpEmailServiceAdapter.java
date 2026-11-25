package com.beyond.bycontract.email.infrastructure.repository;

import com.beyond.bycontract.email.domain.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmtpEmailServiceAdapter implements EmailRepository {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private void sendHtmlEmail(String toEmail, String subject, String templateName, Context context) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // True activates "multipart" mode (mandatory to attach images)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("contact@groupebeyond.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // Generate final HTML code (will find the file emails/templateName.html)
            String htmlContent = templateEngine.process("emails/"+ templateName, context);
            // true says at Spring that the text is HTML
            helper.setText(htmlContent, true);

            // Attach the logo the invisible manner (Inline)
            // Le nom "logoImage" DOIT correspondre au "cid:logoImage" dans le HTML
            ClassPathResource logo = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", logo);

            mailSender.send(message);
            log.info("Email '{}' envoye avec succes a {}", templateName, toEmail);
        }catch (MessagingException e) {
            log.error("Échec de l'envoi de l'email '{}' à {}", templateName, toEmail, e);
            throw new RuntimeException("Erreur technique lors de l'envoi de l'email.");
        }
    }

    @Async
    @Override
    public void sendPasswordResetEmail(String toEmail, String resetUrl) {
        Context context = new Context();
        context.setVariable("resetUrl", resetUrl);
        sendHtmlEmail(toEmail, "Réinitialisation de votre mot de passe - BYCONTRACT", "reset-password", context);
    }

    @Async
    @Override
    public void sendTrialEndingEmail(String toEmail, String firstName, String frontEndUrl) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("frontEndUrl", frontEndUrl);
        sendHtmlEmail(toEmail, "Votre essai gratuit se termine dans 3 jours - BYCONTRACT", "trial-ending", context);
    }

    @Async
    @Override
    public void sendPaymentFailedEmail(String toEmail, String firstName, String invoiceUrl) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("invoiceUrl", invoiceUrl);
        sendHtmlEmail(toEmail, "Action requise : Échec de votre paiement - BYCONTRACT", "payment-failed", context);
    }

    @Async
    @Override
    public void sendSubscriptionCanceledEmail(String toEmail, String firstName, String frontEndUrl) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("frontEndUrl", frontEndUrl);
        sendHtmlEmail(toEmail, "Confirmation d'annulation de votre abonnement - BYCONTRACT", "subscription-canceled", context);
    }

    @Async
    @Override
    public void sendTrialExpiredEmail(String toEmail, String firstName, String frontEndUrl) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("frontEndUrl", frontEndUrl);
        sendHtmlEmail(toEmail, "Votre essai gratuit est terminé - BYCONTRACT", "trial-expired", context);
    }

    @Async
    @Override
    public void sendOtpEmail(String to, String firstName, String otpCode) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("otpCode", otpCode);
        sendHtmlEmail(to, "Votre code de vérification : " + otpCode + " - BYCONTRACT", "otp-verification", context);
    }
}
