package com.springx.api.research;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResearchRequest(@NotBlank @Size(max = 2000) String query) {}