package net.migwel.tournify.store;

import net.migwel.tournify.data.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
}
