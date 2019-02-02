package net.migwel.tournify.core.store;

import net.migwel.tournify.core.data.Tournament;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, Long> {

    Tournament findByUrl(String url);
}
