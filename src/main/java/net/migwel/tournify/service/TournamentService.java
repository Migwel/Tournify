package net.migwel.tournify.service;

import net.migwel.tournify.Updates;
import net.migwel.tournify.data.Tournament;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface TournamentService {

    @Nonnull
    String normalizeUrl(String tournamentUrl);

    @Nonnull
    Updates updateTournament(String url);

    @Nullable
    Tournament getTournament(String url);
}
