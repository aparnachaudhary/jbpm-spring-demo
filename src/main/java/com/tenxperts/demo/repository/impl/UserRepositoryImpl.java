/**
 *
 *
 */

package com.tenxperts.demo.repository.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.tenxperts.demo.entity.User;
import com.tenxperts.demo.repository.UserRepository;

/**
 * 
 * @author Aparna Chaudhary
 */
@Repository("userRepository")
public class UserRepositoryImpl extends AbstractReadWriteRepository<User> implements UserRepository {

    protected UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public User findByUserIdPassword(String username, String password) {
        return (User) getSession().createCriteria(User.class).add(Restrictions.eq("username", username))
                .add(Restrictions.eq("password", password)).uniqueResult();
    }

    @Override
    public User findByUserId(String username) {
        return (User) getSession().createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
    }
}
