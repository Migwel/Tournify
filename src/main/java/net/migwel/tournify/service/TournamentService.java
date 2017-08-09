package net.migwel.tournify.service;

import net.migwel.tournify.data.Tournament;
import org.springframework.stereotype.Service;

@Service
public interface TournamentService {

    Tournament getTournament(String url);
}
