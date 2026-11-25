package com.beyond.bycontract.subscription.infrastructure.scheduler;

import com.beyond.bycontract.email.domain.repository.EmailRepository;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrialExpirationScheduler {

    @Value("${application.cors.allowed-origins}")
    private String frontendUrlOrigin;
    private String frontendUrl = frontendUrlOrigin + "/billing";

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;

    // "0 0 8 * * *" = Tous les jours à 08:00:00 du matin
    @Scheduled(cron = "0 0 8 * * *")
    public void processTrialEndingSoon() {
        log.info("Demarrage: Recherche des periodes d'essai se terminant dans 3 jours");

        LocalDate targetDate = LocalDate.now().plusDays(3);
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX); //23:59:59;

        List<User> users = userRepository.findUsersInTrialEndingBetween(startOfDay, endOfDay);

        for(User user : users) {
            try {
                emailRepository.sendTrialEndingEmail(user.getEmail(), user.getFirstName(), frontendUrl);
            }catch (Exception e) {
                log.error("Echec de l'envoi de l'email J-3 pour l'utilisateur:{}", e, user.getId());
            }
        }
        log.info("Batch termine. {} emails envoyes", users.size());
    }

    // S'exécute tous les jours à 09:00:00 du matin
    @Scheduled(cron = "0 0 9 * * *")
    public void processTrialExpiredToday() {
        log.info("Batch de clôture : Recherche des essais expirés...");
        LocalDateTime now = LocalDateTime.now();
        List<User> expiredUsers = userRepository.findUsersWithExpiredTrials(now);

        for (User user : expiredUsers) {
            try {
                user.expireTrial();
                userRepository.updateUser(user);
                emailRepository.sendTrialExpiredEmail(user.getEmail(), user.getFirstName(), frontendUrl);
                log.info("Essai clôturé pour l'utilisateur : {}", user.getId());
            } catch (Exception e) {
                log.error("Échec de clôture pour : {}", user.getId(), e);
            }
        }
        log.info(" Batch de clôture terminé. {} comptes désactivés.", expiredUsers.size());
    }
}
