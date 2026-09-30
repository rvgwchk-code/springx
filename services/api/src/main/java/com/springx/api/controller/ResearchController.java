package com.springx.api.controller;

import com.springx.api.research.ResearchRequest;
import com.springx.api.research.ResearchResponse;
import com.springx.api.research.ResearchService;
import com.springx.api.retrieval.SearchResponse;
import com.springx.api.retrieval.SearchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
public class ResearchController {
    private final SearchService searchService;
    private final ResearchService researchService;

    public ResearchController(SearchService searchService, ResearchService researchService) {
        this.searchService = searchService;
        this.researchService = researchService;
    }

    @GetMapping("/search")
    public SearchResponse search(
        @RequestParam(name = "q") @NotBlank @Size(max = 2000) String query,
        Authentication authentication
    ) {
        return searchService.search(query, authentication);
    }

    @PostMapping("/research")
    public ResearchResponse research(
        @Valid @RequestBody ResearchRequest request,
        Authentication authentication
    ) {
        return researchService.answer(request.query(), authentication);
    }
}
