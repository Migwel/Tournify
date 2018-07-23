package net.migwel.tournify.client;

import net.migwel.tournify.data.Tournament;

public interface TournamentClient {

    Tournament fetchTournament(String url);
}
