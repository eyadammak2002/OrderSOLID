package com.eya.order.notification;

// ❌ ISP : interface "fourre-tout". Aucune classe n'a besoin des 4 méthodes.
public interface NotificationChannel {
    void sendEmail(String to, String message);
    void sendSms(String phone, String message);
    void sendPush(String deviceToken, String message);
    void sendWhatsapp(String phone, String message);
}