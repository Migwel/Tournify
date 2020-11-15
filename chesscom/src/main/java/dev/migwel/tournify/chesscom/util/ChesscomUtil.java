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

    public static String findRoundSlug(String roundUrl) {
        Pattern p = Pattern.compile(Source.Chesscom.getUrlPattern() + "\\/([0-9a-zA-Z-]+)");
        Matcher m = p.matcher(roundUrl);

        if (m.find()) {
            return m.group(8);
        }

        return null;
    }

    public static String findGroupSlug(String groupUrl) {
        Pattern p = Pattern.compile(Source.Chesscom.getUrlPattern() + "\\/([0-9a-zA-Z-]+)\\/([0-9a-zA-Z-]+)");
        Matcher m = p.matcher(groupUrl);

        if (m.find()) {
            return m.group(9);
        }

        return null;
    }
}
