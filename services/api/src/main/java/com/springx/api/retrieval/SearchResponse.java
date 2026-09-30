package com.springx.api.retrieval;

import java.util.List;

public record SearchResponse(String query, List<SearchEvidence> items) {
    public record SearchEvidence(String evidenceId, String sourceTitle, String location, String excerpt) {}
}