package com.eya.order.util;

import java.util.ArrayList;
import java.util.List;

// ❌ Singleton classique : état global, impossible à remplacer dans un test,
//    et pas thread-safe (deux requêtes simultanées peuvent créer deux instances)
public class AuditLogger {

    private static AuditLogger instance;
    private final List<String> logs = new ArrayList<>();

    private AuditLogger() {}

    public static AuditLogger getInstance() {
        if (instance == null) {
            instance = new AuditLogger();
        }
        return instance;
    }

    public void log(String message) {
        logs.add(message);
        System.out.println("[AUDIT] " + message);
    }
}