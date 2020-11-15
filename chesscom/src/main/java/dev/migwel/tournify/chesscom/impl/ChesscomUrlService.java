package dev.migwel.tournify.chesscom.impl;

import dev.migwel.tournify.core.data.Source;
import dev.migwel.tournify.core.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChesscomUrlService implements UrlService {

    private static final Logger log = LoggerFactory.getLogger(ChesscomUrlService.class);

    @Override
    @Nonnull
    public String normalizeUrl(String url) {
        log.info("Normalizing url: "+ url);
        Pattern p = Pattern.compile(Source.Chesscom.getUrlPattern());
        Matcher m = p.matcher(url);

        if (!m.find()) {
            throw new IllegalArgumentException("Invalid url: "+ url);
        }

        return "https://api.chess.com/pub/tournament/"+ m.group(7);
    }
}
