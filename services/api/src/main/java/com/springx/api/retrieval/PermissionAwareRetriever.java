package com.springx.api.retrieval;

import com.springx.api.identity.UserIdentity;
import java.util.List;

public interface PermissionAwareRetriever {
    List<Evidence> search(String query, UserIdentity identity, int limit);
}