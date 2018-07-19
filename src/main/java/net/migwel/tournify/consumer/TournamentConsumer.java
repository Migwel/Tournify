package net.migwel.tournify.consumer;

import net.migwel.tournify.data.Tournament;

public interface TournamentConsumer {

    Tournament fetchTournament(String url);
}
