package dev.migwel.tournify.core.store;

import dev.migwel.tournify.core.data.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Date;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    Collection<Notification> findByNextDateBeforeAndDone(Date nextDate, boolean done);
}
