/**
 *
 *
 */

package com.tenxperts.demo.repository;

import java.util.List;

import com.tenxperts.demo.entity.Defect;

/**
 * 
 * @author Aparna Chaudhary
 */
public interface DefectRepository extends ReadWriteRepository<Defect> {

    List<Defect> findByIds(List<Long> defectIds);

}
