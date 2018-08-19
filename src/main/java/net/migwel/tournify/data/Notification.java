package net.migwel.tournify.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Subscription subscription;

    private boolean done;

    private int noUpdateRetries;

    private Date startDate;

    private Date nextDate;

    private String content;

    public Notification() {
    }

    public Notification(Subscription subscription, String content, Date startDate, Date nextDate) {
        this.subscription = subscription;
        this.content = content;
        this.startDate = startDate;
        this.nextDate = nextDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getNoUpdateRetries() {
        return noUpdateRetries;
    }

    public void setNoUpdateRetries(int noUpdateRetries) {
        this.noUpdateRetries = noUpdateRetries;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }
}
