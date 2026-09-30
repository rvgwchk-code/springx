package com.springx.api.ingestion;

import java.util.ArrayList;
import java.util.List;

public final class TextChunker {
    private final int chunkSize;
    private final int overlap;

    public TextChunker(int chunkSize, int overlap) {
        if (chunkSize < 1 || overlap < 0 || overlap >= chunkSize) {
            throw new IllegalArgumentException("Chunk size must be positive and overlap smaller than chunk size.");
        }
        this.chunkSize = chunkSize;
        this.overlap = overlap;
    }

    public List<TextChunk> chunk(String text) {
        String normalized = text == null ? "" : text.strip();
        if (normalized.isEmpty()) {
            return List.of();
        }
        List<TextChunk> chunks = new ArrayList<>();
        int start = 0;
        while (start < normalized.length()) {
            int end = Math.min(normalized.length(), start + chunkSize);
            if (end < normalized.length()) {
                int boundary = normalized.lastIndexOf(' ', end);
                if (boundary > start + chunkSize / 2) {
                    end = boundary;
                }
            }
            chunks.add(new TextChunk(normalized.substring(start, end).strip(), start, end));
            if (end == normalized.length()) {
                break;
            }
            start = Math.max(start + 1, end - overlap);
        }
        return List.copyOf(chunks);
    }

    public record TextChunk(String text, int startOffset, int endOffset) {}
}