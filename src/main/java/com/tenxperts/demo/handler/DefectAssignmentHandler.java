/**
 *
 *
 */

package com.tenxperts.demo.handler;

import org.apache.log4j.Logger;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;
import org.springframework.context.ApplicationContext;

import com.tenxperts.demo.entity.Defect;
import com.tenxperts.demo.service.DefectService;
import com.tenxperts.demo.util.SpringApplicationContextUtils;

/**
 * Assignment Handler for assigning defects to developers.
 * 
 * @author Aparna Chaudhary
 */
@SuppressWarnings("serial")
public class DefectAssignmentHandler implements AssignmentHandler {

    private static final Logger logger = Logger.getLogger(DefectAssignmentHandler.class);

    @Override
    public void assign(Assignable assignable, OpenExecution execution) throws Exception {
        ApplicationContext context = SpringApplicationContextUtils.getApplicationContext();
        DefectService defectService = context.getBean(DefectService.class);

        Long defectId = (Long) execution.getVariable("defectId");
        Defect defect = defectService.getDefectById(defectId);
        logger.info("Defect Id: " + defectId + "Assigned To" + defect.getAssignedTo());
        assignable.setAssignee(defect.getAssignedTo());
    }

}
