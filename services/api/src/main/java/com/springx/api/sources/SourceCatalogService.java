package com.springx.api.sources;

import org.springframework.security.core.Authentication;

public interface SourceCatalogService {
    SourceListResponse listFor(Authentication authentication);
}