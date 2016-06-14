package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Notification;
import models.Subscriber;
import models.User;
import play.libs.Json;
import play.mvc.*;

import java.util.LinkedList;
import java.util.List;

public class Application extends Controller {

//    Server server = new Server("localhost", 8025, "/websockets", null, WebSocketServer.class);

    public static Result index() {
        return ok(views.html.index.render());
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result authenticate() {
        ObjectNode result = play.libs.Json.newObject();
        JsonNode requestJson = request().body().asJson();
        String emailId = requestJson.findPath("emailId").asText();
        String password = requestJson.findPath("password").asText();
        User user = User.findByIdAndPassword(emailId, password);

        if (user != null) {
            System.out.println("Before" + session().get("emailId"));
            session().clear();
            session().put("emailId", user.getEmailId());
            System.out.println("After" + session().get("emailId"));

            result.put("successMessage", "Successfull login..congrats!!!!!!");
            return ok(result);
        }
        result.put("errorMessage", "Invalid Credentials .Please try with different username or password");
        return badRequest(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result signUP() {
        ObjectNode result = play.libs.Json.newObject();
        JsonNode requestJson = request().body().asJson();
        String emailId = requestJson.findPath("emailId").asText();
        String password = requestJson.findPath("password").asText();
        String firstName = requestJson.findPath("firstName").asText();
        String lastName = requestJson.findPath("lastName").asText();
        String address = requestJson.findPath("address").asText();
        Integer age = requestJson.findPath("age").asInt();
        if (emailId != null && !"".equals(emailId)) {
            emailId = emailId.trim();
            User user = User.findByEmail(emailId);
            if (user != null)
                result.put("errorMessage", "Please use some other emailId as this user is registered");
        } else {
            result.put("errorMessage", "Please fill emailId");
            return badRequest(result);
        }
        if (password != null && !"".equals(password)) {
            password = password.trim();
        } else {
            result.put("errorMessage", "Password is Empty");
            return badRequest(result);
        }
        if (firstName != null && !"".equals(firstName)) {
            firstName = firstName.trim();
        } else {
            result.put("errorMessage", "First Name is empty");
            return badRequest(result);
        }
        if (lastName != null && !"".equals(lastName)) {
            lastName = lastName.trim();
        } else {
            result.put("errorMessage", "Last Name is empty");
            return badRequest(result);
        }
        if (address != null && !"".equals(address)) {
            address = address.trim();
        } else {
            result.put("errorMessage", "Address is empty");
            return badRequest(result);
        }
        if (age != null) {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmailId(emailId);
            user.setPassword(password);
            user.setAddress(address);
            user.setAge(age);
            user.save();

            result.put("successMessage", "Sign up done successfully");
            return ok(result);

        } else {
            result.put("errorMessage", "age is empty");
            return badRequest(result);
        }

    }

    @Security.Authenticated(value = WebSecurityAuthenticator.class)
    public static Result home() {
        return ok(views.html.editProfile.render());

    }

    @Security.Authenticated(value = WebSecurityAuthenticator.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result editProfile() {
//        String email = ctx().session().get("emailId");
        ObjectNode result = play.libs.Json.newObject();
        JsonNode requestJson = request().body().asJson();
        String emailId = requestJson.findPath("emailId").asText();
        User user = User.findByEmail(emailId);
        String password = requestJson.findPath("password").asText();
        String firstName = requestJson.findPath("firstName").asText();
        String lastName = requestJson.findPath("lastName").asText();
        String address = requestJson.findPath("address").asText();
        int age = requestJson.findPath("age").asInt();

        boolean em = false, pa = false, fi = false, la = false, ad = false, a = false;
        int a1;
        String message = "";
//        if (emailId != null && !"".equals(emailId)) {
//            emailId = email.trim();
//            if (!emailId.equals(user.getEmailId()) && !"".equals(emailId)) {
//                em = true;
//                message = "EmailId: " + emailId;
//                user.setEmailId(emailId);
//            }
//        }
        if (password != null) {
            password = password.trim();
            if (!password.equals(user.getPassword()) && !"".equals(password)) {
                pa = true;
                message += " Password: " + password;
                user.setPassword(password);
            }
        }
        if (firstName != null) {
            firstName = firstName.trim();
            if (!firstName.equals(user.getFirstName()) && !"".equals(firstName)) {
                fi = true;
                message += " FirstName: " + firstName;
                user.setFirstName(firstName);
            }
        }
        if (lastName != null) {
            lastName = lastName.trim();
            if (!lastName.equals(user.getLastName()) && !"".equals(lastName)) {
                la = true;
                message += " LastName: " + lastName;
                user.setLastName(lastName);
            }
        }
        if (address != null) {
            address = address.trim();
            if (!address.equals(user.getAddress()) && !"".equals(address)) {
                la = true;
                message += " Address: " + address;
                user.setAddress(address);
            }
        }
        if (user.getAge() != age) {
            a = true;
            message += " Age:" + age;
            user.setAge(age);
        }

        if (em || pa || fi || la || ad || a) {
            user.update();
        } else {
            result.put("errorMessage", "Please give valid entries");
            return badRequest(result);
        }

        String targetMessage;
        if (!"".equals(message))
            targetMessage = user.getEmailId() + " has updated the following feature :: " + message;
        else targetMessage = "";

        List<Subscriber> subscribers = Subscriber.findBySubscribedId(user.getId());

        for (int i = 0; i < subscribers.size(); i++) {
            Notification.messageToSubscribers(User.findById(subscribers.get(i).getSubscriberId()).getEmailId(), targetMessage);
        }
        result.put("successMessage", "Successfully updated the profile");
        return ok(result);
    }

    @Security.Authenticated(value = WebSecurityAuthenticator.class)
    public static Result getSubscribedPersons() {
        String email = ctx().session().get("emailId");
        User user = User.findByEmail(email);
        List<User> users = new LinkedList<>();
        users.add(0, user);
        users.addAll(Subscriber.findBySubscriberId(user.getId()));
        ObjectMapper mapper = new ObjectMapper();
        String stringJson = null;
        try {
            stringJson = mapper.writeValueAsString(users);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
        return ok(Json.parse(stringJson));
    }

    @Security.Authenticated(value = WebSecurityAuthenticator.class)
    public static Result getUnsubscribedPersons() {
        String email = ctx().session().get("emailId");
        User user = User.findByEmail(email);
        List<User> subscribedUsers = Subscriber.findBySubscriberId(user.getId());
        List<User> allUsers = User.findAll();
        allUsers.removeAll(subscribedUsers);
        allUsers.remove(user);
        ObjectMapper mapper = new ObjectMapper();
        String stringJson = null;
        try {
            stringJson = mapper.writeValueAsString(allUsers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
        return ok(Json.parse(stringJson));
    }

    @Security.Authenticated(value = WebSecurityAuthenticator.class)
    public static Result unsubscribePerson(String emailId) {
        String email = ctx().session().get("emailId");
        System.out.println("Session emailId is :" + email);
        ObjectNode result = Json.newObject();
        User user = User.findByEmail(email);
        User user1 = User.findByEmail(emailId);
        if (user1 != null) {
            Subscriber subscriber1 = Subscriber.finByBothId(user.getId(), user1.getId());
            if (subscriber1 != null) {
                subscriber1.delete();
                result.put("successMessage", "Unsubscribed the person successfully ");
                return ok(result);
            }
        }
        result.put("successMessage", "Already unsubscribed!!!!");
        return ok(result);
    }

    @Security.Authenticated(value = WebSecurityAuthenticator.class)
    public static Result subscribePerson(String emailId) {
        String email = ctx().session().get("emailId");
        System.out.println("Session emailId is :" + email);
        ObjectNode result = Json.newObject();
        User user = User.findByEmail(email);
        User user1 = User.findByEmail(emailId);
        Subscriber subscriber = Subscriber.finByBothId(user.getId(), user1.getId());
        if (subscriber == null) {
            subscriber = new Subscriber();
            subscriber.setSubscriberId(user.getId());
            subscriber.setUser(user1);
            subscriber.save();
            result.put("successMessage", "Added the person successfully to subscribed list");
            return ok(result);
        }
        result.put("successMessage", "Already added");
        return ok(result);
    }

    public static WebSocket<String> socket() {
        return new WebSocket<String>() {
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
                Notification.start(in, out);
            }

        };
    }

    public static Result logout() {
        session().clear();
        return redirect(controllers.routes.Application.index());
    }


}


