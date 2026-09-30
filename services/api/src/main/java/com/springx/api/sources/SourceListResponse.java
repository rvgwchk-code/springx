package com.springx.api.sources;

import java.util.List;

public record SourceListResponse(List<SourceSummary> items) {
    public record SourceSummary(
        String id,
        String name,
        String provider,
        String ownership,
        String status,
        String lastSync
    ) {}
}