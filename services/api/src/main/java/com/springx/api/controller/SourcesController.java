package com.springx.api.controller;

import com.springx.api.sources.SourceCatalogService;
import com.springx.api.sources.SourceListResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sources")
public class SourcesController {
    private final SourceCatalogService sourceCatalogService;

    public SourcesController(SourceCatalogService sourceCatalogService) {
        this.sourceCatalogService = sourceCatalogService;
    }

    @GetMapping
    public SourceListResponse sources(Authentication authentication) {
        return sourceCatalogService.listFor(authentication);
    }

    @GetMapping("/status")
    public SourceStatus status(Authentication authentication) {
        var sources = sourceCatalogService.listFor(authentication).items();
        return new SourceStatus(
            sources.size(),
            sources.stream().filter(source -> "connected".equals(source.status())).count(),
            sources.stream().filter(source -> "syncing".equals(source.status())).count(),
            sources.stream().filter(source -> "error".equals(source.status()) || "warning".equals(source.status())).count()
        );
    }

    public record SourceStatus(long total, long connected, long syncing, long attentionRequired) {}
}
