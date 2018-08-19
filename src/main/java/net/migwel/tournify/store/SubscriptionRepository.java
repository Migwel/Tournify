package net.migwel.tournify.store;

import net.migwel.tournify.data.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    Subscription findByTournamentUrlAndCallbackUrl(String url, String callbackUrl);

    List<Subscription> findByTournamentUrlAndActive(String tournamentUrl, boolean active);
}
