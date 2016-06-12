package models;


import play.db.ebean.Model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "Subscriber")
public class Subscriber extends Model {
    @Id
    private Long id;
    private Long subscriberId;
    @OneToOne()
    @JoinColumn(name = "subscribed_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Finder<Long, Subscriber> find = new Finder<Long, Subscriber>(Long.class,
            Subscriber.class);

    public static Subscriber findById(Long id) {
        return find.byId(id);
    }

    public static List<User> findBySubscriberId(Long subscriberId) {
        List<Subscriber> subscriberList = find.where().eq("subscriber_id", subscriberId).findList();
        List<User> userList = new LinkedList<>();
        for (int i = 0; i < subscriberList.size(); i++) {
            userList.add(subscriberList.get(i).getUser());
        }
        return userList;
    }


    public static List<Subscriber> findBySubscribedId(Long subscribedId) {
        List<Subscriber> subscribers = find.where().eq("subscribed_id", subscribedId).findList();
        return subscribers;
    }

    public static Subscriber finByBothId(Long subscriberId,Long subscribedId){
        return find.where().eq("subscriber_id",subscriberId).eq("subscribed_id",subscribedId).findUnique();
    }
}
