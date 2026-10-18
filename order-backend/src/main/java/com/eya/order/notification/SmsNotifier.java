package com.eya.order.notification;

public class SmsNotifier implements NotificationChannel {

    @Override
    public void sendEmail(String to, String message) {
        throw new UnsupportedOperationException("SmsNotifier ne sait pas envoyer d'email");
    }

    @Override
    public void sendSms(String phone, String message) {
        System.out.println("Envoi d'un SMS à " + phone + " : " + message);
    }

    @Override
    public void sendPush(String deviceToken, String message) {
        throw new UnsupportedOperationException("SmsNotifier ne sait pas envoyer de push");
    }

    @Override
    public void sendWhatsapp(String phone, String message) {
        throw new UnsupportedOperationException("SmsNotifier ne sait pas envoyer de WhatsApp");
    }
}

