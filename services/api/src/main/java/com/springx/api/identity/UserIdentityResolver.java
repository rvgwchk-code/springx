package com.springx.api.identity;

import org.springframework.security.core.Authentication;

public interface UserIdentityResolver {
    UserIdentity resolve(Authentication authentication);
}