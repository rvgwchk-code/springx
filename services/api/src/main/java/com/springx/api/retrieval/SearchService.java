package com.springx.api.retrieval;

import org.springframework.security.core.Authentication;

public interface SearchService {
    SearchResponse search(String query, Authentication authentication);
}