/**
 *
 *
 */

package com.tenxperts.demo.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Application Context Utility.
 * 
 * @author Aparna Chaudhary
 */
@Component
public class SpringApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private SpringApplicationContextUtils() {
        super();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext appContext) {
        applicationContext = appContext;
    }
}
