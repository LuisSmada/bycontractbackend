package com.beyond.bycontract.auth.application.event;

import com.beyond.bycontract.auth.adapter.dto.OtpRequestedEvent;
import com.beyond.bycontract.email.domain.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OtpEmailEventListener {
    private final EmailRepository emailRepo;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOtpRequested(OtpRequestedEvent event) {
        // sendOtpEmail est déjà @Async, donc l'envoi reste non-bloquant
        emailRepo.sendOtpEmail(event.email(), event.firstName(), event.otpCode());
    }
}
