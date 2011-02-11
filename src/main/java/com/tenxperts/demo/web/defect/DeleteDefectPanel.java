package com.tenxperts.demo.web.defect;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tenxperts.demo.entity.Defect;
import com.tenxperts.demo.service.DefectService;

/**
 * 
 * @author Aparna Chaudhary (aparna.chaudhary@gmail.com)
 */
@SuppressWarnings("serial")
public class DeleteDefectPanel extends Panel {

    @SpringBean
    private DefectService defectService;

    public DeleteDefectPanel(String id, final ModalWindow window, final Defect defect) {
        super(id);
        window.setInitialHeight(150);
        window.setInitialWidth(400);
        window.setResizable(false);
        setDefaultModel(new CompoundPropertyModel<Defect>(defect));
        add(new Label("description"));
        add(new Label("resolution"));

        AjaxLink<Defect> yes = new AjaxLink<Defect>("yes") {
            public void onClick(AjaxRequestTarget target) {
                defectService.closeDefect(defect.getId());
                onYes(target);
                window.close(target);
            }
        };
        add(yes);

        AjaxLink<Defect> no = new AjaxLink<Defect>("no") {
            public void onClick(AjaxRequestTarget target) {
                window.close(target);
            }
        };
        add(no);

    }

    protected void onYes(AjaxRequestTarget target) {

    }

}
