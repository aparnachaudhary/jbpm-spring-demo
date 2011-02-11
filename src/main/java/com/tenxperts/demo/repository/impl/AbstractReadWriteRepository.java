package com.tenxperts.demo.repository.impl;

import com.tenxperts.demo.entity.BaseEntity;
import com.tenxperts.demo.repository.ReadWriteRepository;

/**
 * Base class for all Hibernate based repository implementations.
 * 
 * @author Aparna Chaudhary
 */
public abstract class AbstractReadWriteRepository<T extends BaseEntity> extends AbstractReadRepository<T> implements
        ReadWriteRepository<T> {

    /**
     * Constructs a new AbstractReadWriteRepository with a given entity class.
     * 
     * @param clazz The class of the entity this repository manages.
     */
    protected AbstractReadWriteRepository(Class<T> clazz) {
        super(clazz);
    }

    public T save(T t) {
        getHibernateTemplate().save(t);
        getHibernateTemplate().flush();
        return t;
    }

    public T update(T t) {
        T merged = getHibernateTemplate().merge(t);
        getHibernateTemplate().flush();
        return merged;
    }

    public final T removeById(long id) {
        T entity = getById(id);
        if (entity == null) {
            return null;
        }
        getHibernateTemplate().delete(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public void remove(T t) {
        if (getHibernateTemplate().contains(t)) {
            getHibernateTemplate().delete(t);
        } else {
            T attached = (T) getHibernateTemplate().find(getEntityClass().getName(), t.getId());
            getHibernateTemplate().delete(attached);
        }
    }
}
