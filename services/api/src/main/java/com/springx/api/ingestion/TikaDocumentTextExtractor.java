package com.springx.api.ingestion;

import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.exception.WriteLimitReachedException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class TikaDocumentTextExtractor implements DocumentTextExtractor {
    private static final int MAX_EXTRACTED_CHARACTERS = 1_000_000;

    @Override
    public String extract(InputStream input, String contentType) throws IOException {
        BodyContentHandler handler = new BodyContentHandler(MAX_EXTRACTED_CHARACTERS);
        Metadata metadata = new Metadata();
        if (contentType != null && !contentType.isBlank()) {
            metadata.set(Metadata.CONTENT_TYPE, contentType);
        }
        try {
            new AutoDetectParser().parse(input, handler, metadata, new ParseContext());
        } catch (SAXException exception) {
            if (!(exception instanceof WriteLimitReachedException)
                && !(exception.getCause() instanceof WriteLimitReachedException)) {
                throw new IOException("Unable to extract document text.", exception);
            }
        } catch (TikaException exception) {
            throw new IOException("Unable to extract document text.", exception);
        }
        return handler.toString().strip();
    }
}