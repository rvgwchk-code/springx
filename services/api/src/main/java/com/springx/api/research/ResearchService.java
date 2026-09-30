package com.springx.api.research;

import org.springframework.security.core.Authentication;

public interface ResearchService {
    ResearchResponse answer(String query, Authentication authentication);
}