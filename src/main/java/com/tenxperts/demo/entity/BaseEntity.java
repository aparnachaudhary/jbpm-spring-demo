package com.tenxperts.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;


/**
 * Base JPA Entity
 * 
 * @author Aparna Chaudhary
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "emSequence")
    private final Long id;

    @Version
    @Column(name = "version")
    private int version;

    /**
     * Default empty constructor.
     */
    protected BaseEntity() {
        this(null);
    }

    /**
     * Constructs a new BaseEntity with the given id.
     * 
     * @param id The id of this entity.
     */
    protected BaseEntity(Long id) {
        this.id = id;
    }

    // ========================================== Getters/Setters =====================================================

    /**
     * Returns the id of this entity.
     * 
     * @return The id of this entity.
     */
    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    /**
     * @return
     */
    public boolean isPersisted() {
        return id != null;
    }

    // =========================================== Object Methods ======================================================
    /**
     * Determines if this object is equal to another object.
     * 
     * @param o the object to check whether it is equal to this object.
     * @return true if the objects are equal to each other, false otherwise.
     */
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseEntity)) {
            return false;
        }

        final BaseEntity entity = (BaseEntity) o;

        // If id is null, only check for object identity.
        if (getId() == null || entity.getId() == null) {
            return doEquals(o);
        }

        return getId().equals(entity.getId());
    }

    /**
     * Computes the hashcode of this object.
     * 
     * @return integer specifying the hashcode value
     */
    public final int hashCode() {
        if (getId() == null) {
            return doHashCode();
        }
        return (getId().hashCode() + this.getClass().hashCode()) * 29;
    }

    /**
     * Can/Should be overridden by concrete entities. The passed in object can be assumed to be of the same type as the
     * entity class (casting is safe). The method should check business object equality disregarding the id of the
     * entity.
     * 
     * @param object The entity to compare with
     * @return <code>true</code> if this entity equals business-wise to the passed in entity, <code>false</code>
     *         otherwise
     */
    protected boolean doEquals(Object object) {
        return super.equals(object);
    }

    /**
     * Can/Should be overridden by concrete entities. This method should compute the business hashcode of the entity.
     * That is, disregarding the id of the entity.
     * 
     * @return The business hashcode of the entity
     */
    protected int doHashCode() {
        return super.hashCode();
    }

}
