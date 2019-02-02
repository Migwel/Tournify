package net.migwel.tournify.core.store;

import net.migwel.tournify.core.data.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    Subscription findByCallbackUrlAndTournamentUrl(String callbackUrl, String tournamentUrl);
    Subscription findById(UUID id);

    List<Subscription> findByTournamentUrlAndActive(String tournamentUrl, boolean active);
}
