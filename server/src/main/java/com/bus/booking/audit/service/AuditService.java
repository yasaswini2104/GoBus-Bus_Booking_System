package com.bus.booking.audit.service;

public interface AuditService {

    void logAction(
            String action,
            String performedBy,
            String moduleName,
            String description
    );
}