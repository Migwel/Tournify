package dev.migwel.tournify.core.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Source {
    Smashgg("^((http(s)?:\\/\\/)?((www|api)\\.)?)?smash\\.gg\\/tournament\\/([0-9a-zA-Z-]+)\\/event(s)?\\/([0-9a-zA-Z-]+)"),
    Unknown(null),
    ;

    private String urlPattern;

    Source(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    private boolean match(String url) {
        if(urlPattern == null) {
            return false;
        }
        Pattern p = Pattern.compile(urlPattern);
        Matcher m = p.matcher(url);

        return m.find();
    }

    public static Source getSource(String url) {
        for (Source source : values()) {
            if (source.match(url)) {
                return source;
            }
        }
        return Source.Unknown;
    }
}
