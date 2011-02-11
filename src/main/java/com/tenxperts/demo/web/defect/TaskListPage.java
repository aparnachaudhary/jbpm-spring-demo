/**
 *
 *
 */

package com.tenxperts.demo.web.defect;

import java.util.List;

import org.apache.log4j.Logger;
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
import com.tenxperts.demo.service.DefectService;
import com.tenxperts.demo.web.BasePage;

/**
 * 
 * @author Aparna Chaudhary
 */
public class TaskListPage extends BasePage {

    private static final Logger logger = Logger.getLogger(TaskListPage.class);

    @SpringBean
    private DefectService defectService;

    private ModalWindow modal;

    public TaskListPage() {
        showDefectsForReview();
        showDefectsForResolution();
        showDefectsForClosure();

    }

    private void showDefectsForReview() {
        List<Defect> reviewDefects = defectService.findDefectsForReview(getUserName());
        WebMarkupContainer wmc = new WebMarkupContainer("reviewDefectsContainer");
        wmc.setOutputMarkupId(true);

        ListView<Defect> defectListView = new ListView<Defect>("reviewDefect", reviewDefects) {
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
            }
        };
        wmc.add(defectListView);
        add(wmc);
    }

    private void showDefectsForResolution() {
        List<Defect> resolutionDefects = defectService.findDefectsForResolution(getUserName());
        WebMarkupContainer wmc = new WebMarkupContainer("resolveDefectsContainer");
        wmc.setOutputMarkupId(true);

        ListView<Defect> defectListView = new ListView<Defect>("resolveDefect", resolutionDefects) {
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

                Link<Object> resolveLink = new Link<Object>("resolve") {
                    @Override
                    public void onClick() {
                        setResponsePage(new ResolveDefectPage(defect.getId()));
                    }
                };
                item.add(resolveLink);
            }
        };
        wmc.add(defectListView);
        add(wmc);
    }

    private void showDefectsForClosure() {
        List<Defect> resolutionDefects = defectService.findDefectsForClosure(getUserName());
        WebMarkupContainer wmc = new WebMarkupContainer("closeDefectsContainer");
        wmc.setOutputMarkupId(true);

        ListView<Defect> defectListView = new ListView<Defect>("closeDefect", resolutionDefects) {
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
    protected IModel<?> getTitleModel() {
        return new Model<String>("TaskList");
    }

}
