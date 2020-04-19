package dev.migwel.tournify.app;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class PenaltyBox {

    private final Map<String, PenaltyItem> blockedUrls;

    public PenaltyBox() {
        this.blockedUrls = new HashMap<>();
    }

    public boolean isBlocked(String url) {
        PenaltyItem penaltyItem = blockedUrls.get(url);
        if (penaltyItem == null) {
            return false;
        }

        Date releaseDate = penaltyItem.getReleaseDate();

        return releaseDate.after(new Date());
    }

    public void success(String url) {
        blockedUrls.remove(url);
    }

    public void failure(String url) {
        if (!blockedUrls.containsKey(url)) {
            blockedUrls.put(url, PenaltyItem.createItem(url));
            return;
        }
        blockedUrls.get(url).failure();
    }
}
