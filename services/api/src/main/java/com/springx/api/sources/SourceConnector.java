package com.springx.api.sources;

import com.springx.api.identity.UserIdentity;

public interface SourceConnector {
    SourceKind kind();

    boolean isConfigured();

    void synchronize(String connectionId, UserIdentity identity);
}