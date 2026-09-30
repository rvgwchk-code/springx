package com.springx.api.research;

public record ResearchLimits(int maxRounds, int maxToolCalls, int maxResultsPerCall, long maxDurationMillis) {
    public ResearchLimits {
        if (maxRounds < 1 || maxToolCalls < 1 || maxResultsPerCall < 1 || maxDurationMillis < 1) {
            throw new IllegalArgumentException("Research limits must be positive.");
        }
    }

    public static ResearchLimits defaults() {
        return new ResearchLimits(3, 6, 8, 20_000);
    }
}