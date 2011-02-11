/**
 *
 *
 */

package com.tenxperts.demo.repository;

import com.tenxperts.demo.entity.User;

/**
 * 
 * @author Aparna Chaudhary
 */
public interface UserRepository extends ReadWriteRepository<User> {

    User findByUserIdPassword(String username, String password);

    User findByUserId(String username);

}
