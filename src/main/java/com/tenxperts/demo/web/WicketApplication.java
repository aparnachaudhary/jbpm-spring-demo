package com.tenxperts.demo.web;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import com.tenxperts.demo.web.login.LoginPage;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start
 * class.
 * 
 * @see wicket.myproject.Start#main(String[])
 * 
 * @author Richard Wilkinson - richard.wilkinson@jweekend.com
 * 
 */
public class WicketApplication extends WebApplication {
    /**
     * Constructor
     */
    public WicketApplication() {
    }

    @Override
    protected void init() {
        super.init();

        // Make injection of spring beans in wicket-related classes possible
        // using @SpringBean.
        addComponentInstantiationListener(new SpringComponentInjector(this));

        new AnnotatedMountScanner().scanPackage("com.tenxperts.demo.web").mount(this);

        // Wicket markup setting.
        getMarkupSettings().setStripComments(true);
        getMarkupSettings().setStripWicketTags(true);
        getMarkupSettings().setDefaultBeforeDisabledLink("");
        getMarkupSettings().setDefaultAfterDisabledLink("");
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return LoginPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new WicketJBPMSession(request);
    }

}
