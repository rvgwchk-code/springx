package com.springx.api.research;

import com.springx.api.error.ProviderUnavailableException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UnconfiguredResearchService implements ResearchService {
    @Override
    public ResearchResponse answer(String query, Authentication authentication) {
        throw new ProviderUnavailableException("research model and retrieval");
    }
}