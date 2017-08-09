package net.migwel.tournify.service;

import net.migwel.tournify.data.Sources;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlService {

    public Sources parseUrl(String url) {
        String smashGGPattern = "^(http(s)?:\\/\\/(www\\.)?)?smash\\.gg\\/tournament\\/([0-9a-zA-Z-]+)";

        Pattern p = Pattern.compile(smashGGPattern);
        Matcher m = p.matcher(url);

        if (m.find()) {
            return Sources.Smashgg;
        }
        return Sources.Unknown;
    }
}
