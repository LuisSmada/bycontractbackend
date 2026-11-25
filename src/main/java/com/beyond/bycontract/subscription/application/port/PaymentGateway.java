package com.beyond.bycontract.subscription.application.port;

import java.util.UUID;

public interface PaymentGateway {

    String createCheckoutSession(UUID idUser, String email, String stripeIdCustomer, String idPrice, String successUrl, String cancelUrl);
}
