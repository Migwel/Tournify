package net.migwel.tournify.smashgg.service.impl;

import net.migwel.tournify.service.UrlService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("SmashggUrlService")
public class SmashggUrlService implements UrlService {

    @Override
    public String formatUrl(String url) throws IllegalArgumentException{
        Pattern p = Pattern.compile(smashggURLPattern);
        Matcher m = p.matcher(url);

        if (!m.find()) {
            throw new IllegalArgumentException("Invalid url: "+ url);
        }

        return "https://api.smash.gg/tournament/"+ m.group(6);

    }
}
