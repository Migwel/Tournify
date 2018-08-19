package net.migwel.tournify.store;

import net.migwel.tournify.data.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    List<Notification> findByNextDateBeforeAndDone(Date nextDate, boolean done);
}
