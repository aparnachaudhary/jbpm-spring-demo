package com.tenxperts.demo.repository;

import java.util.List;

/**
 * A basic Repository interface which knows how to read data.
 * 
 * @author Aparna Chaudhary
 */
public interface ReadRepository<T> {

    /**
     * Returns the object from the data store that is identified by the given id.
     * 
     * @param id The id of the requested object in the data store
     * @return The object identified by the given id in the data store
     */
    T getById(long id);

    /**
     * Returns a list of all entities of type T or an empty list if none can be found.
     * 
     * @return a list of all entities of type T or an empty list if none can be found
     */
    List<T> findAll();
    
    List<Long> findAllIds();
    
}
