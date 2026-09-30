package com.springx.api.ingestion;

import java.io.IOException;
import java.io.InputStream;

public interface DocumentTextExtractor {
    String extract(InputStream input, String contentType) throws IOException;
}