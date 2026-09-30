package com.springx.api.research;

import com.springx.api.retrieval.Evidence;
import java.util.List;

public interface ResearchModelProvider {
    ResearchPlan plan(ResearchContext context);

    List<ResearchClaim> answer(ResearchContext context);

    record ResearchContext(
        String question,
        List<Evidence> evidence,
        int remainingToolCalls,
        String systemPolicy
    ) {}

    record ResearchPlan(List<String> queries, boolean enoughEvidence) {}

    record ResearchClaim(String text, List<String> evidenceIds) {}
}