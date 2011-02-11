/**
 *
 *
 */

package com.tenxperts.demo.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.api.task.Task;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenxperts.demo.entity.Defect;
import com.tenxperts.demo.entity.DefectStatus;
import com.tenxperts.demo.repository.DefectRepository;
import com.tenxperts.demo.service.DefectService;

/**
 * 
 * @author Aparna Chaudhary
 */
@Service("defectService")
@Transactional(readOnly = true)
public class DefectServiceImpl implements DefectService {

    private static final Logger logger = Logger.getLogger(DefectServiceImpl.class);

    private static final String DEFECT_TRACKING_PROCESS_KEY = "DefectTracking";

    private List<String> processDefinitions;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ExecutionService executionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    HistoryService historyService;
    @Autowired
    DefectRepository defectRepository;

    @PostConstruct
    public void setupProcessDefinitions() {
        try {
            for (String processDefinition : processDefinitions) {
                NewDeployment deployment = repositoryService.createDeployment();
                deployment.addResourceFromUrl(new ClassPathResource(processDefinition).getURL());
                deployment.deploy();
            }
        } catch (IOException e) {
            logger.info("IOException occurred: ", e);
            throw new RuntimeException("An error occured while trying to deploy a process definition", e);
        }
    }

    @Override
    public Defect getDefectById(Long defectId) {
        return defectRepository.getById(defectId);
    }

    @Override
    @Transactional(readOnly = false)
    public Defect createDefect(Defect defect) {
        defect.setCreatedDate(new LocalDate());
        defect.setStatus(DefectStatus.NEW);
        defectRepository.save(defect);

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("defectId", defect.getId());
        vars.put("assignee", defect.getAssignedTo());
        executionService.startProcessInstanceByKey(DEFECT_TRACKING_PROCESS_KEY, vars, Long.toString(defect.getId()));
        logger.info("Task assigned to: " + defect.getAssignedTo() + " : "
                + taskService.findPersonalTasks(defect.getAssignedTo()));
        return defect;
    }

    @Override
    @Transactional(readOnly = false)
    public Defect reviewDefect(Long defectId, String assignedTo) {
        Defect existingDefect = defectRepository.getById(defectId);

        String existingAsignee = existingDefect.getAssignedTo();
        logger.info("Existing Assignee: " + existingAsignee);

        existingDefect.setStatus(DefectStatus.ASSIGNED);
        existingDefect.setAssignedTo(assignedTo);
        defectRepository.update(existingDefect);

        List<Task> tasks = taskService.findPersonalTasks(existingAsignee);
        for (Task task : tasks) {
            if (task.getActivityName().equalsIgnoreCase("reviewDefect")) {
                Long taskDefectId = (Long) taskService.getVariable(task.getId(), "defectId");
                if (taskDefectId.equals(defectId)) {
                    taskService.completeTask(task.getId());
                    logger.info("Reviewed Defect" + task.getActivityName());
                }
            }
        }
        logger.info("Task assigned to: " + existingAsignee + " : " + taskService.findPersonalTasks(existingAsignee));
        logger.info("Task assigned to: " + assignedTo + " : " + taskService.findPersonalTasks(assignedTo));

        return existingDefect;
    }

    @Override
    @Transactional(readOnly = false)
    public Defect markResolved(Long defectId, String resolution) {
        Defect existingDefect = getDefectById(defectId);
        existingDefect.setResolution(resolution);
        existingDefect.setStatus(DefectStatus.RESOLVED);

        defectRepository.update(existingDefect);

        List<Task> tasks = taskService.findPersonalTasks(existingDefect.getAssignedTo());
        for (Task task : tasks) {
            if (task.getActivityName().equalsIgnoreCase("resolveDefect")) {
                Long taskDefectId = (Long) taskService.getVariable(task.getId(), "defectId");
                if (taskDefectId.equals(defectId)) {
                    taskService.completeTask(task.getId());
                    logger.info("Resolved Defect" + task.getActivityName());
                }
            }
        }

        logger.info("Task assigned to: " + existingDefect.getAssignedTo() + " : "
                + taskService.findPersonalTasks(existingDefect.getAssignedTo()));

        return existingDefect;
    }

    @Override
    @Transactional(readOnly = false)
    public void closeDefect(Long defectId) {
        Defect defect = getDefectById(defectId);
        defect.setStatus(DefectStatus.CLOSED);
        defectRepository.update(defect);

        String processInstanceKey = signalExecution(defectId);

        logger.info("Ended Process Instance: "
                + executionService.createProcessInstanceQuery().processInstanceKey(processInstanceKey).uniqueResult());
        logger.info("Process Instance Moved to History: "
                + historyService.createHistoryProcessInstanceQuery().processInstanceKey(processInstanceKey)
                        .uniqueResult());

    }

    private String signalExecution(Long defectId) {
        String processInstanceKey = Long.toString(defectId);
        for (ProcessInstance processInstance : executionService.createProcessInstanceQuery()
                .processInstanceKey(processInstanceKey).list()) {
            if (processInstance.isActive("closeDefect")) {
                executionService.signalExecutionById(processInstance.getId());
            }
        }
        return processInstanceKey;
    }

    /**
     * A simple mutator to facilitate configuration.
     * 
     * @param definitions
     */
    public void setProcessDefinitions(List<String> definitions) {
        this.processDefinitions = definitions;
    }

    @Override
    public List<Defect> findDefectsForReview(String username) {
        return findDefects(username, "reviewDefect");
    }

    @Override
    public List<Defect> findDefectsForResolution(String username) {
        return findDefects(username, "resolveDefect");
    }

    @Override
    public List<Defect> findDefectsForClosure(String username) {
        List<Long> defectIds = new ArrayList<Long>();
        for (ProcessInstance processInstance : executionService.createProcessInstanceQuery().list()) {
            if (processInstance.isActive("closeDefect")) {
                Long defectId = (Long) executionService.getVariable(processInstance.getId(), "defectId");
                defectIds.add(defectId);
            }
        }
        return defectRepository.findByIds(defectIds);
    }

    private List<Defect> findDefects(String username, String taskName) {
        List<Task> tasks = taskService.findPersonalTasks(username);
        List<Long> defectIds = new ArrayList<Long>();
        for (Task task : tasks) {
            if (task.getActivityName().equalsIgnoreCase(taskName)) {
                Long defectId = (Long) taskService.getVariable(task.getId(), "defectId");
                defectIds.add(defectId);
            }
        }
        return defectRepository.findByIds(defectIds);
    }

}
