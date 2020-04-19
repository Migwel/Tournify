package dev.migwel.tournify.app;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PenaltyItem {

    private String url;
    private Date releaseDate;
    private int nbFailures;

    private final static long MIN_MS = 60 * 1000L;
    private final static long HOUR_MS = 60 * MIN_MS;
    private final static List<Long> penaltyDelays = List.of(
            MIN_MS,
            2 * MIN_MS,
            5 * MIN_MS,
            15 * MIN_MS,
            30 * MIN_MS,
            HOUR_MS,
            2 * HOUR_MS,
            4 * HOUR_MS
    );

    private PenaltyItem(String url, Date releaseDate, int nbFailures) {
        this.url = url;
        this.releaseDate = releaseDate;
        this.nbFailures = nbFailures;
    }

    public static PenaltyItem createItem(String url) {
        return new PenaltyItem(url, new Date(new Date().getTime() + penaltyDelays.get(0)), 0);
    }

    public void failure() {
        nbFailures++;
        Long delay = getDelay();
        releaseDate = new Date(new Date().getTime() + delay);
    }

    private Long getDelay() {
        int delayKey = Math.min(nbFailures, penaltyDelays.size() - 1);
        return penaltyDelays.get(delayKey);
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PenaltyItem that = (PenaltyItem) o;
        return nbFailures == that.nbFailures &&
                Objects.equals(url, that.url) &&
                Objects.equals(releaseDate, that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, releaseDate, nbFailures);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PenaltyItem{");
        sb.append("url='").append(url).append('\'');
        sb.append(", releaseDate=").append(releaseDate);
        sb.append(", nbFailures=").append(nbFailures);
        sb.append('}');
        return sb.toString();
    }
}
