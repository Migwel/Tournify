package dev.migwel.tournify.chesscom.util;

import dev.migwel.tournify.core.data.Source;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChesscomUtil {

    public static String findTournamentSlug(String eventUrl) {
        Pattern p = Pattern.compile(Source.Chesscom.getUrlPattern());
        Matcher m = p.matcher(eventUrl);

        if (m.find()) {
            return m.group(7);
        }

        return null;
    }
}
