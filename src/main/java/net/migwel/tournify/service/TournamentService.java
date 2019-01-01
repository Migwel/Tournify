package net.migwel.tournify.service;

import net.migwel.tournify.data.SetUpdate;
import net.migwel.tournify.data.Tournament;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface TournamentService {

    @Nonnull
    String normalizeUrl(String tournamentUrl);

    @Nonnull
    List<SetUpdate> updateTournament(String url);

    @Nullable
    Tournament getTournament(String url);
}
