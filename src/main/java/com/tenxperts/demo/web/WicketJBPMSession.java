package com.tenxperts.demo.web;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

import com.tenxperts.demo.entity.User;

/**
 *
 * @author Aparna Chaudhary
 */
@SuppressWarnings("serial")
public class WicketJBPMSession extends WebSession {

    private User user;

    public WicketJBPMSession(Request request) {
        super(request);
    }

    public static WicketJBPMSession get() {
        return (WicketJBPMSession) Session.get();
    }

    public synchronized User getUser() {
        return user;
    }

    public synchronized void setUser(User user) {
        this.user = user;
    }

}
