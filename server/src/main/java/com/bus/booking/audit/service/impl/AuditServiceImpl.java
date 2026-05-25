package com.bus.booking.audit.service.impl;

import com.bus.booking.audit.entity.AuditLog;
import com.bus.booking.audit.repository.AuditLogRepository;
import com.bus.booking.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl
        implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void logAction(
            String action,
            String performedBy,
            String moduleName,
            String description
    ) {

        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .performedBy(performedBy)
                .moduleName(moduleName)
                .description(description)
                .build();

        auditLogRepository.save(auditLog);

        log.info(
                "Audit log created for action: {}",
                action
        );
    }
}