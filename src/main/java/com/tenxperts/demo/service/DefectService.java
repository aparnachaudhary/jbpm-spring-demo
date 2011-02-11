/**
 *
 *
 */

package com.tenxperts.demo.service;

import java.util.List;

import com.tenxperts.demo.entity.Defect;

/**
 * 
 * @author Aparna Chaudhary
 */
public interface DefectService {

    Defect createDefect(Defect defect);

    /**
     * @param defectId defect identifier
     * @return
     */
    Defect getDefectById(Long defectId);

    Defect reviewDefect(Long defectId, String assignedTo);

    /**
     * @param defectId
     */
    Defect markResolved(Long defectId, String resolution);

    /**
     * @param defectId
     */
    void closeDefect(Long defectId);

    List<Defect> findDefectsForReview(String username);

    List<Defect> findDefectsForResolution(String username);
    
    List<Defect> findDefectsForClosure(String username);
    
}
