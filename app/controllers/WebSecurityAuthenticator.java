package controllers;

import models.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by zemoso014 on 6/9/16.
 */
public class WebSecurityAuthenticator extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context ctx) {
        String email = ctx.session().get("emailId");
        User user = User.findByEmail(email);
        ctx.args.put("user", user);
        return email;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(routes.Application.index());
    }
}
