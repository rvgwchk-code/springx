package com.springx.api.error;

public class ProviderUnavailableException extends RuntimeException {
    public ProviderUnavailableException(String capability) {
        super("The " + capability + " provider is not configured.");
    }
}