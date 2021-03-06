package dev.migwel.tournify.core.store;

import dev.migwel.tournify.core.data.TournamentTracking;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Date;

public interface TrackingRepository extends CrudRepository<TournamentTracking, Long> {
    Collection<TournamentTracking> findByNextDateBeforeAndDone(Date nextDate, boolean done);
}
