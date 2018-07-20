package net.migwel.tournify.service;

import net.migwel.tournify.consumer.TournamentConsumer;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.data.TournamentTracking;
import net.migwel.tournify.store.TournamentRepository;
import net.migwel.tournify.store.TrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

//TODO: I'm not sure I'm happy with an abstract class. I'd rather have an interface but not sure how to do that...
public abstract class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TrackingRepository trackingRepository;

    protected Tournament getTournament(TournamentConsumer tournamentConsumer,
                                     String formattedUrl) {
        Tournament tournament = tournamentRepository.findByUrl(formattedUrl);
        if(tournament != null) {
            return tournament;
        }

        tournament = tournamentConsumer.fetchTournament(formattedUrl);
        tournamentRepository.save(tournament);
        trackTournament(tournament);
        return tournament;
    }

    private void trackTournament(Tournament tournament) {
        TournamentTracking tournamentTracking = new TournamentTracking(tournament, new Date(), new Date());
        trackingRepository.save(tournamentTracking);
    }

    public abstract Tournament getTournament(String url);
}
