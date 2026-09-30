package com.springx.api.ingestion;

import java.util.List;

public interface EmbeddingProvider {
    List<float[]> embed(List<String> texts);
}