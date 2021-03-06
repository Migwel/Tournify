package dev.migwel.tournify.smashgg.impl;

import dev.migwel.tournify.core.data.Source;
import dev.migwel.tournify.core.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Immutable
public class SmashggUrlService implements UrlService {

    private static final Logger log = LoggerFactory.getLogger(SmashggUrlService.class);

    @Override
    @Nonnull
    public String normalizeUrl(String url) {
        log.info("Normalizing url: "+ url);
        Pattern p = Pattern.compile(Source.Smashgg.getUrlPattern());
        Matcher m = p.matcher(url);

        if (!m.find()) {
            throw new IllegalArgumentException("Invalid url: "+ url);
        }

        return "https://api.smash.gg/tournament/"+ m.group(6) +"/event/"+ m.group(8);
    }
}
