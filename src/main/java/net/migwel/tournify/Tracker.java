package net.migwel.tournify;

import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.data.TournamentTracking;
import net.migwel.tournify.service.ServiceFactory;
import net.migwel.tournify.service.TournamentService;
import net.migwel.tournify.store.TrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class Tracker implements ApplicationRunner { //TODO: Tracking should be more fine-grained (events or sets)

    private static final Logger log = LoggerFactory.getLogger(Tracker.class);

    private final static long SEC = 1000;
    private final static long MIN = 60 * SEC;
    private final static long HOUR = 60 * MIN;
    private final static long DAY = 24 * HOUR;
    private final static long WEEK = 7 * DAY;
    private final static long TRACKING_WAIT_MS = 5 * SEC;
    private final static long[] NO_UPDATE_WAIT_MS = {MIN, MIN, MIN, 2 * MIN, 2 * MIN, 5 * MIN, 5 * MIN, 5 * MIN, 15 * MIN, 30 * MIN, HOUR, 2 * HOUR, 4 * HOUR, DAY, 2 * DAY, WEEK};

    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    private ServiceFactory serviceFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            log.info("Start tracking");
            startTracking();
            Thread.sleep(TRACKING_WAIT_MS);
        }
    }

    private void startTracking() {
        List<TournamentTracking> trackingList = trackingRepository.findByNextDateBeforeAndDone(new Date(), false);
        log.info("Tracking list size: "+ trackingList.size());
        for(TournamentTracking tracking : trackingList) {
            Tournament tournament = tracking.getTournament();
            TournamentService tournamentService = serviceFactory.getTournamentService(tournament.getUrl());
            boolean isSame = !tournamentService.updateTournament(tournament.getUrl()); //TODO: This should be done in a separate thread

            if(isSame) {
                tracking.setNextDate(computeNextDate(tracking.getNoUpdateRetries()));
                tracking.setNoUpdateRetries(tracking.getNoUpdateRetries() + 1);
                if(tracking.getNoUpdateRetries() > NO_UPDATE_WAIT_MS.length - 1) { //TODO: add other ways of setting tracking to done (e.g. tournament is finished)
                    tracking.setDone(true);
                }
            }
            else {
                tracking.setNextDate(new Date());
                tracking.setNoUpdateRetries(0);
            }

            trackingRepository.save(tracking);
        }

    }

    private Date computeNextDate(int noUpdateRetries) {
        return new Date(System.currentTimeMillis() + computeTimeToAdd(noUpdateRetries));
    }

    private long computeTimeToAdd(int noUpdateRetries) {
        if(noUpdateRetries >= NO_UPDATE_WAIT_MS.length) {
            return NO_UPDATE_WAIT_MS[NO_UPDATE_WAIT_MS.length - 1];
        }

        return NO_UPDATE_WAIT_MS[noUpdateRetries];
    }
}
