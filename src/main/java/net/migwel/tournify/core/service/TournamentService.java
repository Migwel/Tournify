package net.migwel.tournify.core.service;

import net.migwel.tournify.communication.commons.Updates;
import net.migwel.tournify.core.data.Tournament;
import net.migwel.tournify.smashgg.data.Participant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface TournamentService {

    @Nonnull
    String normalizeUrl(String tournamentUrl);

    @Nonnull
    Updates updateTournament(String url);

    @Nullable
    Tournament getTournament(String url);

    @Nonnull
    List<Participant> getParticipants(String url);
}
