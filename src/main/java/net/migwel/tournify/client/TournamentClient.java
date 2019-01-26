package net.migwel.tournify.client;

import net.migwel.tournify.data.Tournament;

import javax.annotation.Nullable;

public interface TournamentClient {

    @Nullable
    Tournament fetchTournament(Tournament oldTournament, String url);
}
