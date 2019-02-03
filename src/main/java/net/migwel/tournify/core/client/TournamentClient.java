package net.migwel.tournify.core.client;

import net.migwel.tournify.core.data.Tournament;
import net.migwel.tournify.smashgg.data.Participant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface TournamentClient {

    @Nullable
    Tournament fetchTournament(Tournament oldTournament, String url);

    @Nonnull
    List<Participant> getParticipants(String url);
}
