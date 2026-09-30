package com.springx.api.sources;

import com.springx.api.error.ProviderUnavailableException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UnconfiguredSourceCatalogService implements SourceCatalogService {
    @Override
    public SourceListResponse listFor(Authentication authentication) {
        throw new ProviderUnavailableException("source catalog storage");
    }
}