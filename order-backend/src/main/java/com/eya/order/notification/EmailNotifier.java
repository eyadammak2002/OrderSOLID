package com.eya.order.notification;

public class EmailNotifier implements NotificationChannel {

    @Override
    public void sendEmail(String to, String message) {
        System.out.println("Envoi d'un email à " + to + " : " + message);
    }

    // ❌ ISP : forcé d'implémenter des méthodes qui n'ont aucun sens ici
    @Override
    public void sendSms(String phone, String message) {
        throw new UnsupportedOperationException("EmailNotifier ne sait pas envoyer de SMS");
    }

    @Override
    public void sendPush(String deviceToken, String message) {
        throw new UnsupportedOperationException("EmailNotifier ne sait pas envoyer de push");
    }

    @Override
    public void sendWhatsapp(String phone, String message) {
        throw new UnsupportedOperationException("EmailNotifier ne sait pas envoyer de WhatsApp");
    }
}