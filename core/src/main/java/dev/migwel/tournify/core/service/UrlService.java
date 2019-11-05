package dev.migwel.tournify.core.service;

import javax.annotation.Nonnull;

public interface UrlService {
    @Nonnull
    String normalizeUrl(String url);
}
