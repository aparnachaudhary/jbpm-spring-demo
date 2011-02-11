/**
 * 
 */

package com.tenxperts.demo.web.common.wicket.component;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

/**
 * 
 * @author Aparna Chaudhary (aparna.chaudhary@gmail.com)
 */
public interface ItemSelectionChangedCallback extends Serializable {

    void onItemSelectionChanged(AjaxRequestTarget target, IModel item);
}
