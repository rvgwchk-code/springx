package com.springx.api.error;

import java.util.Map;

public record ApiError(ErrorBody error) {
    public record ErrorBody(String code, String message, Map<String, String> details) {}
}