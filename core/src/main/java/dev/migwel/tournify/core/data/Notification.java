package dev.migwel.tournify.core.data;

import javax.annotation.Nonnull;
import javax.persistence.Column;
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

    @Column(columnDefinition="TEXT")
    private String content;

    public Notification() {
    }

    public Notification(Subscription subscription, String content, @Nonnull Date startDate, @Nonnull Date nextDate) {
        this.subscription = subscription;
        this.content = content;
        this.startDate = new Date(startDate.getTime());
        this.nextDate = new Date(nextDate.getTime());
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
        if(startDate == null) {
            return null;
        }
        return new Date(startDate.getTime());
    }

    public void setStartDate(Date startDate) {
        if(startDate == null) {
            return;
        }
        this.startDate = new Date(startDate.getTime());
    }

    public Date getNextDate() {
        if(nextDate == null) {
            return null;
        }
        return new Date(nextDate.getTime());
    }

    public void setNextDate(Date nextDate) {
        if(nextDate == null) {
            return;
        }
        this.nextDate = new Date(nextDate.getTime());
    }
}
