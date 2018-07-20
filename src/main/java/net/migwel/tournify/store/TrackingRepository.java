package net.migwel.tournify.store;

import net.migwel.tournify.data.TournamentTracking;
import org.springframework.data.repository.CrudRepository;

public interface TrackingRepository extends CrudRepository<TournamentTracking, Long> {
}
