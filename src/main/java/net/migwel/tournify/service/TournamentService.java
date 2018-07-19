package net.migwel.tournify.service;

import net.migwel.tournify.consumer.TournamentConsumer;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.store.TournamentRepository;
import org.springframework.stereotype.Service;

@Service
public interface TournamentService {

    Tournament getTournament(String url);

    default Tournament getTournament(TournamentConsumer tournamentConsumer,
                                     TournamentRepository tournamentRepository,
                                     String formattedUrl) {
        Tournament tournament = tournamentRepository.findByUrl(formattedUrl);
        if(tournament != null) {
            return tournament;
        }

        tournament = tournamentConsumer.fetchTournament(formattedUrl);
        tournamentRepository.save(tournament);
        return tournament;
    }
}
