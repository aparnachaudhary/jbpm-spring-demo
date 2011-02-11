/**
 *
 *
 */

package com.tenxperts.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenxperts.demo.entity.User;
import com.tenxperts.demo.repository.UserRepository;
import com.tenxperts.demo.service.UserService;

/**
 * 
 * @author Aparna Chaudhary
 */
@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUserIdPassword(username, password);
        return user != null ? true : false;
    }

    @Override
    public User findUser(String username) {
        return userRepository.findByUserId(username);
    }

}
