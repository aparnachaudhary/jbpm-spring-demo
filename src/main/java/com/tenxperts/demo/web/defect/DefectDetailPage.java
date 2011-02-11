package com.tenxperts.demo.web.defect;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tenxperts.demo.entity.Defect;
import com.tenxperts.demo.repository.DefectRepository;
import com.tenxperts.demo.web.BasePage;

/**
 * Wicket page to display Defect details.
 * 
 * @author Aparna Chaudhary (aparna.chaudhary@gmail.com)
 */
public class DefectDetailPage extends BasePage {

    @SpringBean
    private DefectRepository defectRepository;

    public DefectDetailPage(PageParameters parameters) {
        this(parameters.getLong("id"));
    }

    public DefectDetailPage(Long id) {
        Defect defect = defectRepository.getById(id);
        add(new Label("description", defect.getDescription()));
        add(new Label("resolution", defect.getResolution()));
        add(new Label("createdDate", defect.getCreatedDate().toString()));
        add(new Label("createdBy", defect.getCreatedBy()));
        add(new Label("assignedTo", defect.getAssignedTo()));
        add(new Label("status", defect.getStatus().name()));
    }

    @Override
    protected IModel<String> getTitleModel() {
        return new Model<String>("DefectDetails");
    }

}
