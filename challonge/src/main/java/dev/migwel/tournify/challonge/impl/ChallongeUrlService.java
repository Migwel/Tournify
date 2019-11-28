package dev.migwel.tournify.challonge.impl;

import dev.migwel.tournify.core.data.Source;
import dev.migwel.tournify.core.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChallongeUrlService implements UrlService {

    private static final Logger log = LoggerFactory.getLogger(ChallongeUrlService.class);

    @Override
    @Nonnull
    public String normalizeUrl(String url) {
        log.info("Normalizing url: "+ url);
        Pattern p = Pattern.compile(Source.Challonge.getUrlPattern());
        Matcher m = p.matcher(url);

        if (!m.find()) {
            throw new IllegalArgumentException("Invalid url: "+ url);
        }

        return "https://api.challonge.com/v1/tournaments/"+ m.group(6);
    }
}
