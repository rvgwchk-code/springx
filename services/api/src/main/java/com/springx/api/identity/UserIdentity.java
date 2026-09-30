package com.springx.api.identity;

public record UserIdentity(String tenantId, String userId) {
    public UserIdentity {
        if (tenantId == null || tenantId.isBlank() || userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("Tenant and user identifiers are required.");
        }
    }
}