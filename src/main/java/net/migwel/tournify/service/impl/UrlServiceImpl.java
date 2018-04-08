package net.migwel.tournify.service.impl;

import net.migwel.tournify.data.Sources;
import net.migwel.tournify.service.UrlService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlServiceImpl implements UrlService {

    private static String tournamentPattern = "^((http(s)?:\\/\\/)?((www|api)\\.)?)?smash\\.gg\\/tournament\\/([0-9a-zA-Z-]+)";

    @Override
    public Sources parseUrl(String url) {
        Pattern p = Pattern.compile(tournamentPattern);
        Matcher m = p.matcher(url);

        if (m.find()) {
            return Sources.Smashgg;
        }

        return Sources.Unknown;
    }

    @Override
    public String formatUrl(String url) throws IllegalArgumentException{
        String pattern = tournamentPattern;

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(url);

        if (!m.find()) {
            throw new IllegalArgumentException("Invalid url: "+ url);
        }

        return "https://api.smash.gg/tournament/"+ m.group(6);

    }
}
