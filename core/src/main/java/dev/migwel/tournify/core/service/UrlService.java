package dev.migwel.tournify.core.service;

import dev.migwel.tournify.core.data.Source;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface UrlService {

    String smashggURLPattern = "^((http(s)?:\\/\\/)?((www|api)\\.)?)?smash\\.gg\\/tournament\\/([0-9a-zA-Z-]+)\\/event(s)?\\/([0-9a-zA-Z-]+)";

    default Source parseUrl(String url) {
        Pattern p = Pattern.compile(smashggURLPattern);
        Matcher m = p.matcher(url);

        if (m.find()) {
            return Source.Smashgg;
        }

        return Source.Unknown;
    }

    @Nonnull
    String normalizeUrl(String url);
}
