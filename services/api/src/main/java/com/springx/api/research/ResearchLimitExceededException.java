package com.springx.api.research;

public class ResearchLimitExceededException extends RuntimeException {
    public ResearchLimitExceededException(String message) {
        super(message);
    }
}