package net.migwel.tournify.core.client;

import net.migwel.tournify.core.data.Tournament;

import javax.annotation.Nullable;

public interface TournamentClient {

    @Nullable
    Tournament fetchTournament(Tournament oldTournament, String url);
}
