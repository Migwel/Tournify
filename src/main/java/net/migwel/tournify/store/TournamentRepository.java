package net.migwel.tournify.store;

import net.migwel.tournify.data.Tournament;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, Long> {
}
