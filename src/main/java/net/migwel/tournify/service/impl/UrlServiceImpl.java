package net.migwel.tournify.service.impl;

import net.migwel.tournify.data.Sources;
import net.migwel.tournify.service.UrlService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlServiceImpl implements UrlService {

    public Sources parseUrl(String url) {
        String smashGGPattern = "^(http(s)?:\\/\\/(www\\.)?)?smash\\.gg\\/tournament\\/([0-9a-zA-Z-]+)";

        Pattern p = Pattern.compile(smashGGPattern);
        Matcher m = p.matcher(url);

        if (m.find()) {
            return Sources.Smashgg;
        }

        return Sources.Unknown;
    }

    public String formatUrl(String url) throws IllegalArgumentException{
        String pattern = "^(http(s)?:\\/\\/((www|api)\\.)?)?smash\\.gg\\/tournament\\/([0-9a-zA-Z-]+)\\/event\\/([0-9a-zA-Z-]+)";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(url);

        if (!m.find()) {
            throw new IllegalArgumentException("Invalid url: "+ url);
        }

        return "https://api.smash.gg/tournament/"+ m.group(5);

    }
}
