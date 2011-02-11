/**
 * 
 */

package com.tenxperts.demo.web.common.wicket.component;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.string.Strings;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.util.Assert;

import com.inmethod.grid.IDataSource;
import com.inmethod.grid.IGridColumn;
import com.inmethod.grid.column.PropertyColumn;
import com.inmethod.grid.datagrid.DataGrid;
import com.inmethod.grid.datagrid.DefaultDataGrid;

/**
 * 
 * @author Aparna Chaudhary (aparna.chaudhary@gmail.com)
 */
public class DataGridFactory implements Serializable {

    /** The log4j logger for this class. */
    private static final Logger logger = Logger.getLogger(DataGridFactory.class);

    private final List<IGridColumn> columns;

    private final IDataSource dataSource;

    private ItemSelectionChangedCallback callback;

    private static final String DEFAULT_DATE_FORMAT = "MM-dd-yyyy HH:mm";

    private static final String DEFAULT_GRID_ID = "grid";

    /**
     * Constructs a new DataGridFactory.
     * 
     * @param dataSource the datasouce used by the datagrid to build
     */
    public DataGridFactory(IDataSource dataSource) {
        Assert.notNull(dataSource, "Argument 'dataSource' must not be null");
        columns = new ArrayList<IGridColumn>();
        this.dataSource = dataSource;
    }

    /**
     * Create a new default datagrid If there is no call back set the onItemSelectionChanged method will no be called.
     * 
     * @param id the given wicket id. If the id is null the default id "grid" is used.
     * @return the created datagrid
     */
    public final DefaultDataGrid createDefaultDataGrid(String id) {
        String dataGridId = id;
        if (id == null) {
            dataGridId = DEFAULT_GRID_ID;
        }

        if (hasGridAtLeastOneColumn()) {
            throw new IllegalStateException("Grid needs at least one column, there are no columns added.");
        }

        return new DefaultDataGrid(dataGridId, dataSource, columns) {
            @Override
            public void onItemSelectionChanged(IModel item, boolean newValue) {
                AjaxRequestTarget target = AjaxRequestTarget.get();
                if (callback != null) {
                    callback.onItemSelectionChanged(target, item);
                }
            }

            @Override
            protected void onRowClicked(AjaxRequestTarget target, IModel rowModel) {
                if (callback != null) {
                    callback.onItemSelectionChanged(target, rowModel);
                }
            }
        };
    }

    /**
     * Create a new datagrid If there is no call back set the onItemSelectionChanged method will no be called.
     * 
     * @param id the given wicket id. If the id is null the default id "grid" is used.
     * @return the created datagrid
     */
    public final DataGrid createDataGrid(String id) {
        String dataGridId = id;
        if (id == null) {
            dataGridId = DEFAULT_GRID_ID;
        }

        if (hasGridAtLeastOneColumn()) {
            throw new IllegalStateException("Grid needs at least one column, there are no columns added.");
        }

        return new DataGrid(dataGridId, dataSource, columns) {
            @Override
            public void onItemSelectionChanged(IModel item, boolean newValue) {
                AjaxRequestTarget target = AjaxRequestTarget.get();
                if (callback != null) {
                    callback.onItemSelectionChanged(target, item);
                }
            }

            @Override
            protected void onRowClicked(AjaxRequestTarget target, IModel rowModel) {
                if (callback != null) {
                    callback.onItemSelectionChanged(target, rowModel);
                }
            }
        };
    }

    /**
     * Checks if there is at least one column.
     * 
     * @return true is there is at least one column otherwise false
     */
    private boolean hasGridAtLeastOneColumn() {
        return columns.size() < 1;
    }

    /**
     * Add a column to the datagrid factory.
     * 
     * @param column the column to add. Make sure to add the column before calling method createDataGrid.
     */
    public final void addColumn(IGridColumn column) {
        Assert.notNull(column, "Parameter 'column' must not be null.");
        columns.add(column);
    }

    /**
     * Add a new PropertyColumn to the table grid.
     * 
     * @param columnName the name of the column, this name is equals to the resource key used to display the header of
     *            the column. The columName must be unique.
     * @param propertyExpression propertyExpression the property expression used to get the displayed value for row
     *            object (i.e. patient.firstName ), the property expression is also used as the sortproperty.
     * @param size the size of the column (equals or > than 0)
     * @param resizable true if the column is resizable, false if not
     * @param reorderable true if the column is reorderable, false if not
     */
    public final void addDefaultPropertyColumn(String columnName, String propertyExpression, int size,
            boolean resizable, boolean reorderable) {
        Assert.hasText(columnName, "Argument 'columnName' must contain text");
        Assert.hasText(propertyExpression, "Argument 'propertyExpression' must contain text");
        if (size < 0) {
            throw new IllegalArgumentException("Argument 'size' must be equals or > than 0");
        }

        PropertyColumn column = new PropertyColumn(createResourceModelForResourceKey(columnName), propertyExpression,
                propertyExpression);
        column.setInitialSize(size);
        column.setMaxSize(size);
        column.setResizable(resizable);
        column.setReorderable(reorderable);
        column.setWrapText(true);
        columns.add(column);
    }

    /**
     * Add a new PropertyColumn to the table grid.
     * 
     * @param columnName the name of the column, this name is equals to the resource key used to display the header of
     *            the column. The columName must be unique.
     * @param propertyExpression propertyExpression the property expression used to get the displayed value for row
     *            object (i.e. patient.firstName ), the property expression is also used as the sortproperty.
     * @param highlightCssClassName the name of the CSS class that is attached to the selected row. (can be used to
     *            highlight)
     * @param size the size of the column (equals or > than 0)
     * @param resizable true if the column is resizable, false if not
     * @param reorderable true if the column is reorderable, false if not
     */
    public final void addSelectablePropertyColumn(String columnName, String propertyExpression,
            final String highlightCssClassName, int size, boolean resizable, boolean reorderable) {
        Assert.hasText(columnName, "Argument 'columnName' must contain text");
        Assert.hasText(propertyExpression, "Argument 'propertyExpression' must contain text");
        Assert.hasText(highlightCssClassName, "Argument 'highlightCssClassName' must contain text");
        if (size < 0) {
            throw new IllegalArgumentException("Argument 'size' must be equals or > than 0");
        }

        PropertyColumn column = new PropertyColumn(createResourceModelForResourceKey(columnName), propertyExpression,
                propertyExpression) {
            @Override
            public String getCellCssClass(IModel rowModel, int rowNum) {
                String retType = super.getCellCssClass(rowModel, rowNum);
                Collection<IModel> selectedItems = this.getGrid().getSelectedItems();
                if (selectedItems != null) {
                    for (IModel item : selectedItems) {
                        Object selectedObject = item.getObject();
                        Object rowObject = rowModel.getObject();
                        if (rowObject.equals(selectedObject)) {
                            retType = highlightCssClassName;
                        }
                    }
                }
                return retType;
            }

        };

        column.setInitialSize(size);
        column.setMaxSize(size);
        column.setResizable(resizable);
        column.setReorderable(reorderable);
        column.setWrapText(true);
        columns.add(column);
    }

    /**
     * Add a property linked column to the grid. Clicking this column will execute the given callback.
     * 
     * @param columnName the name of the column, this name is equals to the resource key used to display the header of
     *            the column. The columName must be unique.
     * @param propertyExpression propertyExpression the property expression used to get the displayed value for row
     *            object (i.e. patient.firstName ), the property expression is also used as the sortproperty.
     * @param onClickCallback the callback that will be called when the button is clicked
     * @param size the size of the column
     * @param resizable true if the column is resizable, false if not
     * @param reorderable true if the column is reorderable, false if not
     */
    public final void addClickablePropertyColumn(String columnName, final String propertyExpression,
            final AjaxButtonColumnCallback onClickCallback, int size, boolean resizable, boolean reorderable) {
        Assert.hasText(columnName, "Argument 'columnName' must contain text");
        Assert.hasText(propertyExpression, "Argument 'imageName' must contain text");
        Assert.notNull(onClickCallback, "Argument 'onClickCallback' must contain a callback");
        if (size < 0) {
            throw new IllegalArgumentException("Argument 'size' must be equals or > than 0");
        }

        PropertyColumn column = new PropertyColumn(createResourceModelForResourceKey(columnName), propertyExpression,
                propertyExpression) {
            public Component newCell(WebMarkupContainer parent, String componentId, final IModel rowModel) {
                return new AjaxLink(componentId, new Model(propertyExpression)) {
                    public void onClick(AjaxRequestTarget target) {
                        onClickCallback.onClick(target, rowModel);
                    }
                };
            }
        };

        column.setInitialSize(size);
        column.setResizable(resizable);
        column.setReorderable(reorderable);
        column.setWrapText(true);
        columns.add(column);
    }

    /**
     * Add a new Property column to the table grid. If the given property contains no date an empty String will be
     * placed in the cell of the column.
     * 
     * @param columnName the name of the column, this name is equals to the resource key used to display the header of
     *            the column. The columName must be unique.
     * @param propertyExpression propertyExpression the property expression used to get the displayed value for row
     *            object (i.e. patient.firstName ), the property expression is also used as the sortproperty.
     * @param dateFormatResourceKey the dateFormat resource bundle key
     * @param size the size of the column (equals or > than 0)
     * @param resizable true if the column is resizable, false if not
     * @param reorderable true if the column is reorderable, false if not
     */
    public final void addDatePropertyColumn(String columnName, String propertyExpression,
            final String dateFormatResourceKey, int size, boolean resizable, boolean reorderable) {
        Assert.hasText(columnName, "Argument 'columnName' must contain text");
        Assert.hasText(propertyExpression, "Argument 'propertyExpression' must contain text");
        Assert.hasText(dateFormatResourceKey, "Argument 'dateFormatResourceKey' must contain text");
        if (size < 0) {
            throw new IllegalArgumentException("Argument 'size' must be equals or more than 0");
        }

        PropertyColumn dateColumn = new PropertyColumn(new ResourceModel(columnName), propertyExpression,
                propertyExpression) {
            protected CharSequence convertToString(Object object) {
                if (object == null) {
                    return "";
                }

                Date date = getDateFromObject(object);
                if (date == null) {
                    return "This is not a date";
                }

                String dateFormat = getResourceKeyValueFromResourceBundle(dateFormatResourceKey, DEFAULT_DATE_FORMAT);

                try {
                    return new SimpleDateFormat(dateFormat).format(date);
                }
                catch (IllegalArgumentException iae) {
                    logger.warn("Cannot format the date with date format " + dateFormat);

                    try {
                        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
                    }
                    catch (IllegalArgumentException e) {
                        logger.error("");
                        return "Cannot format date";
                    }
                }
            }
        };
        dateColumn.setInitialSize(size);
        dateColumn.setResizable(resizable);
        dateColumn.setReorderable(reorderable);
        dateColumn.setWrapText(true);
        columns.add(dateColumn);
    }

    /**
     * Return the date converted or casted from the object. Returns null if the object is not an instance of
     * java.util.Date or org.joda.time.LocalDate
     * 
     * @param object the object that can be converted of casted to a java.util.Date.
     * @return Returns the date converted or casted from the object otherwise null.
     */
    protected final Date getDateFromObject(Object object) {
        Date date = null;
        if (object instanceof Date) {
            date = (Date) object;
        }

        if (object instanceof LocalDate) {
            date = ((LocalDate) object).toDateMidnight().toDate();
        }

        if (object instanceof DateTime) {
            date = ((DateTime) object).toDate();
        }
        return date;
    }

    /**
     * Returns the value of the resource key. The default value will be returned when the given resource key is null or
     * when the resouce key isn't found in the resource bundle. If the given defaultValue is null the return value will
     * also be null if the given resource key is null or when the resouce key isn't found in resource bundle.
     * <p/>
     * A warning is logged when the resource bundle isn't found.
     * 
     * @param resourceKey the given resource key to find the value for in the resource bundle
     * @param defaultValue the given default value returned when the given resource key is null or when the resouce key
     *            isn't found in the resource bundle
     * @return return the value of the given resourceKey if exists otherwise null
     */
    protected final String getResourceKeyValueFromResourceBundle(String resourceKey, String defaultValue) {
        if (!hasResourceKeyText(resourceKey)) {
            return defaultValue;
        }

        ResourceModel resourceModel = createResourceModelForResourceKey(resourceKey);
        try {
            return resourceModel.getObject().toString();
        }
        catch (MissingResourceException mre) {
            logger.warn("Key " + resourceKey + " in the resource bundle is missing.");
            return defaultValue;
        }
    }

    /**
     * Returns true if the given resource key is not null and contains text otherwise return false.
     * 
     * @param resourceKey the given resourcekey to check
     * @return boolean true if the given resource key is not null and contains text otherwise return false
     */
    protected final boolean hasResourceKeyText(String resourceKey) {
        return !Strings.isEmpty(resourceKey);
    }

    /**
     * @param resourceKey the resource key to create a Resource model for
     * @return the resource model for the given resource key
     */
    private ResourceModel createResourceModelForResourceKey(String resourceKey) {
        return new ResourceModel(resourceKey);
    }

    /**
     * Add a property column to the grid. The value of the cell in the column will contain the value from the resource
     * bundle. The value of the resouce key contains two parts the given prefix and the value of the property.
     * <p/>
     * For example: if the given prefix is "taskInboxOverviewPanel.priority." and the actual value of the property is 1
     * the resource key that needs to be found is: "taskInboxOverviewPanel.priority.1".
     * <p/>
     * If the resource key is not found in the resource bundle an the following String is returned
     * "?? <missing resourcekey> ??" In this example the returned String will be
     * "??taskInboxOverviewPanel.priority.1??".
     * 
     * @param columnName the name of the column, this name is equals to the resource key used to display the header of
     *            the column. The columName must be unique.
     * @param propertyExpression the property expression used to get the displayed value for row object. (i.e.
     *            patient.firstName ), the property expression is also used as the sortproperty.
     * @param prefix the prefix of the resource key
     * @param size the size of the column
     * @param resizable true if the column is resizable, false if not
     * @param reorderable true if the column is reorderable, false if not
     */
    public final void addResourcePropertyColumn(String columnName, final String propertyExpression,
            final String prefix, int size, boolean resizable, boolean reorderable) {

        Assert.hasText(columnName, "Argument 'columnName' must contain text");
        Assert.hasText(propertyExpression, "Argument 'propertyExpression' must contain text");
        if (size < 0) {
            throw new IllegalArgumentException("Argument 'size' must be equals or > than 0");
        }

        PropertyColumn column = new PropertyColumn(new ResourceModel(columnName), propertyExpression,
                propertyExpression) {
            protected CharSequence convertToString(Object object) {
                String resourceValueString = "";
                if (object == null) {
                    return resourceValueString;
                }
                String value = object.toString();
                String key = prefix + value;

                try {
                    return getResourceKeyValueFromResourceBundle(key, null);
                }
                catch (MissingResourceException mre) {
                    logger.warn("Key " + key + "is missing in the resource bundle.");
                    resourceValueString = "??" + key + "??";
                }
                return resourceValueString;
            }
        };
        column.setInitialSize(size);
        column.setResizable(resizable);
        column.setReorderable(reorderable);
        column.setWrapText(true);
        columns.add(column);
    }

    /**
     * Sets the callback
     * 
     * @param callback the given callback
     */
    public final void setItemSelectionChangedCallback(ItemSelectionChangedCallback callback) {
        this.callback = callback;
    }

}