package com.tenxperts.demo.repository.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.tenxperts.demo.entity.BaseEntity;
import com.tenxperts.demo.repository.ReadRepository;

/**
 * A base class for all Hibernate based read repository implementations.
 * 
 * @author Aparna Chaudhary
 */
public abstract class AbstractReadRepository<T extends BaseEntity> implements ReadRepository<T> {

    private final Class<T> clazz;

    private HibernateTemplate hibernateTemplate;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    /**
     * Constructs a new AbstractReadRepository with a given entity class.
     * 
     * @param clazz The class of the entity this repository manages.
     */
    protected AbstractReadRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected final Class<T> getEntityClass() {
        return clazz;
    }

    /**
     * {@inheritDoc}
     */
    public T getById(long id) {
        return hibernateTemplate.get(clazz, id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return hibernateTemplate.find("select e from " + getEntityClass().getName() + " e");
    }

    @SuppressWarnings("unchecked")
    public List<Long> findAllIds() {
        return hibernateTemplate.find("select e.id from " + getEntityClass().getName() + " e");
    }

    @SuppressWarnings("unchecked")
    protected List<T> find(String queryString, Object... args) {
        return hibernateTemplate.find(queryString, args);
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public Session getSession() {
        return getHibernateTemplate().getSessionFactory().getCurrentSession();
    }
}
