package com.tenxperts.demo.web.defect;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tenxperts.demo.entity.Defect;
import com.tenxperts.demo.repository.DefectRepository;
import com.tenxperts.demo.web.BasePage;

/**
 * 
 * @author Aparna Chaudhary (aparna.chaudhary@gmail.com)
 */
@SuppressWarnings("serial")
public class ListDefectsPage extends BasePage {

    @SpringBean
    private DefectRepository defectRepository;

    private ModalWindow modal;

    public ListDefectsPage() {
        List<Defect> defects = defectRepository.findAll();
        WebMarkupContainer wmc = new WebMarkupContainer("defectsContainer");
        wmc.setOutputMarkupId(true);

        ListView<Defect> defectListView = new ListView<Defect>("defect", defects) {
            @Override
            protected void populateItem(final ListItem<Defect> item) {
                item.setOutputMarkupId(true);

                final Defect defect = item.getModelObject();
                item.setModel(new CompoundPropertyModel<Defect>(defect));

                item.add(new Label("description"));
                item.add(new Label("resolution"));
                item.add(new Label("createdDate"));
                item.add(new Label("createdBy"));
                item.add(new Label("assignedTo"));
                item.add(new Label("status"));

                Link<Object> reviewLink = new Link<Object>("review") {
                    @Override
                    public void onClick() {
                        setResponsePage(new ReviewDefectPage(defect.getId()));
                    }
                };
                item.add(reviewLink);

                Link<Object> resolveLink = new Link<Object>("resolve") {
                    @Override
                    public void onClick() {
                        setResponsePage(new ResolveDefectPage(defect.getId()));
                    }
                };
                item.add(resolveLink);

                AjaxLink<Object> closeLink = new AjaxLink<Object>("close") {
                    public void onClick(AjaxRequestTarget target) {
                        DeleteDefectPanel deletePanel = new DeleteDefectPanel(modal.getContentId(), modal, defect) {
                            protected void onYes(AjaxRequestTarget target) {
                                item.setVisible(false);
                                target.addComponent(item);
                            }
                        };
                        modal.setContent(deletePanel);
                        modal.show(target);
                    }
                };
                item.add(closeLink);
            }
        };
        wmc.add(defectListView);

        add(wmc);

        modal = new ModalWindow("modal");
        add(modal);

    }

    @Override
    protected IModel<String> getTitleModel() {
        return new Model<String>("ListDefect");
    }
}
