package com.beyond.bycontract.subscription.application.dto;

import java.time.LocalDateTime;

public record InvoiceResponse(
        String id,
        String number,           // Ex: "4A1B2C-0001"
        LocalDateTime date,      // Date d'émission (UTC)
        Double amount,           // Montant en euros (ex: 24.90)
        String currency,         // Ex: "eur"
        String status,           // "paid", "open", "void", "uncollectible"
        String invoicePdfUrl,    // Lien de téléchargement direct du PDF
        String hostedInvoiceUrl
) {
}
