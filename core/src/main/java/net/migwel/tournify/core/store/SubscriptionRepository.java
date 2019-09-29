package net.migwel.tournify.core.store;

import net.migwel.tournify.core.data.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.UUID;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    Subscription findByCallbackUrlAndTournamentUrl(String callbackUrl, String tournamentUrl);
    Subscription findById(UUID id);

    Collection<Subscription> findByTournamentUrlAndActive(String tournamentUrl, boolean active);
}
