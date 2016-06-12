package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;


@Entity
@Table(name = "User")
public class User extends Model {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private int age;
    private String emailId;
    private String password;
    private Subscriber subscriber;

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public String toString() {
        return this.getFirstName() + " " + this.getLastName() + " :" + this.getId() + " and password:" + this.getPassword();
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static Finder<Long, User> find = new Finder<Long, User>(Long.class,
            User.class);

    public static List<User> findAll() {
        return find.all();
    }

    public static User findById(Long id) {
        return find.byId(id);
    }


    public static User findByIdAndPassword(String emailId, String password) {
        return find.where().eq("email_id", emailId).eq("password", password).findUnique();
    }

    public static User findByEmail(String emailId) {
        return find.where().eq("email_id", emailId).findUnique();
    }

}
