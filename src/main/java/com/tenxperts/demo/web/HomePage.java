package com.tenxperts.demo.web;

import org.apache.wicket.PageParameters;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Homepage
 */
public class HomePage extends BasePage {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor that is invoked when page is invoked without a session.
     * 
     * @param parameters Page parameters
     */
    public HomePage(final PageParameters parameters) {


    }

    @Override
    protected IModel<?> getTitleModel() {
        return new Model<String>("HOME PAGE");
    }
}
