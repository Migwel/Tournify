package net.migwel.tournify.store;

import net.migwel.tournify.data.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    Subscription findByCallbackUrlAndTournamentUrl(String callbackUrl, String tournamentUrl);
    Subscription findByTournamentUrl(String tournamentUrl);
    Subscription findByCallbackUrl(String callbackUrl);

    List<Subscription> findByTournamentUrlAndActive(String tournamentUrl, boolean active);
}
