package com.springx.api.retrieval;

import com.springx.api.error.ProviderUnavailableException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UnconfiguredSearchService implements SearchService {
    @Override
    public SearchResponse search(String query, Authentication authentication) {
        throw new ProviderUnavailableException("hybrid retrieval");
    }
}