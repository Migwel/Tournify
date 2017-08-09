package net.migwel.tournify.service;

import net.migwel.tournify.data.Tournament;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("SmashggTournamentService")
public class SmashggTournamentService implements TournamentService {
    @Override
    public Tournament getTournament(String url) {
        return new Tournament(null, url, "Amsterdam", new Date());
    }
}
