package net.migwel.tournify.service;

import net.migwel.tournify.data.Sources;

public interface UrlService {

    Sources parseUrl(String url);
    String formatUrl(String url);
}
