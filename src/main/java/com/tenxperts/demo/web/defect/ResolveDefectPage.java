package com.tenxperts.demo.web.defect;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tenxperts.demo.entity.Defect;
import com.tenxperts.demo.service.DefectService;
import com.tenxperts.demo.web.BasePage;

/**
 * 
 * @author Aparna Chaudhary (aparna.chaudhary@gmail.com)
 */
@SuppressWarnings("serial")
public class ResolveDefectPage extends BasePage {

    @SpringBean
    private DefectService defectService;

    private Defect defect;
    private Long defectId;

    public ResolveDefectPage() {
        this(null);
    }

    public ResolveDefectPage(Long id) {
        this.defect = defectService.getDefectById(id);
        this.defectId = defect.getId();

        FeedbackPanel msgs = new FeedbackPanel("msgs");
        add(msgs);

        Form<Defect> form = new Form<Defect>("form") {
            protected void onSubmit() {
                Defect defect = ResolveDefectPage.this.defect;
                defect = defectService.markResolved(defectId, defect.getResolution());
                setResponsePage(new DefectDetailPage(defect.getId()));
            }
        };
        form.setModel(new CompoundPropertyModel<Defect>(defect));

        TextField<String> description = new TextField<String>("description");
        description.setRequired(true);
        description.setEnabled(false);
        form.add(description);

        TextField<String> resolution = new TextField<String>("resolution");
        resolution.setRequired(true);
        form.add(resolution);

        TextField<String> assignedTo = new TextField<String>("assignedTo");
        assignedTo.setRequired(true);
        form.add(assignedTo);

        add(form);

    }

    @Override
    protected IModel<String> getTitleModel() {
        return new Model<String>("Create / Edit Defect");
    }

}
