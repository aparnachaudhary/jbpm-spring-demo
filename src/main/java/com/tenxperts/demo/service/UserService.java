/**
 *
 *
 */

package com.tenxperts.demo.service;

import com.tenxperts.demo.entity.User;

/**
 * 
 * @author Aparna Chaudhary
 */
public interface UserService {

    boolean authenticateUser(String username, String password);

    User findUser(String username);

}
