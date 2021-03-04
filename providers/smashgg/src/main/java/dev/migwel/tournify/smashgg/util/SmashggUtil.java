package dev.migwel.tournify.smashgg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SmashggUtil {
    private SmashggUtil() {
        //util
    }

    public static String findEventSlug(String eventUrl) {
        String smashggTournamentURLPattern = "^https:\\/\\/api.smash.gg\\/(tournament\\/[A-Za-z0-9-]+\\/event\\/[A-Za-z0-9-]+)";
        Pattern p = Pattern.compile(smashggTournamentURLPattern);
        Matcher m = p.matcher(eventUrl);

        if (m.find()) {
            return m.group(1);
        }

        return null;
    }
}