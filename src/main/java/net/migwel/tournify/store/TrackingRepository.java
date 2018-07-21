package net.migwel.tournify.store;

import net.migwel.tournify.data.TournamentTracking;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface TrackingRepository extends CrudRepository<TournamentTracking, Long> {
    List<TournamentTracking> findByNextDateBeforeAndDone(Date nextDate, boolean done);
}
