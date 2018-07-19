package net.migwel.tournify.store;

import net.migwel.tournify.data.Subscription;
import net.migwel.tournify.data.Tournament;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    public Subscription findByTournamentUrlAndCallbackUrl(String url, String callbackUrl);
}
