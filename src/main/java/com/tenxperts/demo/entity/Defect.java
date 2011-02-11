/**
 *
 *
 */

package com.tenxperts.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 * 
 * @author Aparna Chaudhary
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "defect")
@SequenceGenerator(name = "emSequence", sequenceName = "defect_id_seq", allocationSize = 1)
public class Defect extends BaseEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "resolution")
    private String resolution;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DefectStatus status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public DefectStatus getStatus() {
        return status;
    }

    public void setStatus(DefectStatus status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

}
