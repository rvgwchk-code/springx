package com.springx.api.research;

import java.util.List;

public record ResearchResponse(String query, String answer, boolean insufficientEvidence, List<Citation> citations) {}