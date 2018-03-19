/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.preferencepages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.tools400.java.scoped.variables.core.JavaScopedTemplateVariablesPlugin;
import de.tools400.java.scoped.variables.core.Messages;
import de.tools400.java.scoped.variables.core.model.Category;
import de.tools400.java.scoped.variables.core.model.ScopedValue;
import de.tools400.java.scoped.variables.core.model.Variable;
import de.tools400.java.scoped.variables.core.preferences.Preferences;
import de.tools400.java.scoped.variables.core.swt.widgets.WidgetFactory;

public class VariablesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private static final String NEW_VARIABLE = "<New variable>"; //$NON-NLS-1$
    private static final String NEW_SCOPED_VALUE = "<New>"; //$NON-NLS-1$

    public static final String SCOPE = "SCOPE"; //$NON-NLS-1$
    public static final String VALUE = "VALUE"; //$NON-NLS-1$
    public static final String[] PROPS = { SCOPE, VALUE };

    // Section: createHeader()
    private Combo cbCategory;
    private Category[] categories;

    // Section: createEditorPanel()
    private Label lblNewEdit;
    private Text txtName;
    private Text txtDescription;
    private Button btnCreate;
    private TableViewer tvValues;

    // Section: createSelectorPanel()
    private List listVariablesSelector;

    public VariablesPreferencePage() {
        super();

        setPreferenceStore(JavaScopedTemplateVariablesPlugin.getDefault().getPreferenceStore());
        getPreferenceStore();
    }

    public void init(IWorkbench workbench) {
    }

    @Override
    public Control createContents(Composite parent) {

        Composite container = new Composite(parent, SWT.NONE);
        final GridLayout gridLayout = new GridLayout(2, false);
        container.setLayout(gridLayout);

        createHeader(container);
        createSelectorPanel(container);
        createEditorPanel(container);

        setScreenToValues();

        return container;
    }

    private void createHeader(Composite parent) {

        createLabel(parent, Messages.Label_Category);

        cbCategory = WidgetFactory.createReadOnlyCombo(parent, SWT.NONE);
        cbCategory.setLayoutData(createLayoutData());
        cbCategory.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                performCategoryChangedEvent();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        createLabel(parent, Messages.Label_Variables, 2);
    }

    private void createSelectorPanel(Composite parent) {

        Composite cpVariables = new Composite(parent, SWT.NONE);
        cpVariables.setLayout(createLayoutNoMargins(1));
        cpVariables.setLayoutData(new GridData(GridData.FILL_BOTH));

        listVariablesSelector = new List(cpVariables, SWT.BORDER);
        listVariablesSelector.setLayoutData(new GridData(GridData.FILL_BOTH));
        listVariablesSelector.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                performVariableChangedEvent();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        Menu popupMenu = new Menu(listVariablesSelector);
        popupMenu.addMenuListener(new VariablesContextMenu(popupMenu));

        listVariablesSelector.setMenu(popupMenu);
    }

    private void createEditorPanel(Composite parent) {

        Composite cpEditor = new Composite(parent, SWT.NONE);
        cpEditor.setLayout(createLayoutNoMargins(1));
        cpEditor.setLayoutData(new GridData(GridData.FILL_BOTH));

        lblNewEdit = createLabel(cpEditor, "", 2); //$NON-NLS-1$

        Composite cpEdit = new Composite(cpEditor, SWT.NONE);
        cpEdit.setLayout(createLayoutNoMargins(3));
        cpEdit.setLayoutData(new GridData(GridData.FILL_BOTH));

        createLabel(cpEdit, Messages.Label_Name);
        txtName = WidgetFactory.createText(cpEdit, SWT.NONE);
        txtName.setLayoutData(createLayoutData(1));
        txtName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                Variable variable = getSelectedVariable();
                if (variable != null) {
                    variable.setName(txtName.getText());
                }
                setControlsEnablement();
            }
        });

        btnCreate = WidgetFactory.createPushButton(cpEdit, "Create");
        btnCreate.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 2));
        btnCreate.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                performCreateVariable(txtName.getText(), txtDescription.getText());
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        createLabel(cpEdit, Messages.Label_Description);
        txtDescription = WidgetFactory.createText(cpEdit, SWT.NONE);
        txtDescription.setLayoutData(createLayoutData(1));
        txtDescription.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                Variable variable = getSelectedVariable();
                if (variable != null) {
                    variable.setDescription(txtDescription.getText());
                }
                setControlsEnablement();
            }
        });

        tvValues = new TableViewer(cpEdit, SWT.FULL_SELECTION | SWT.BORDER);

        Table tblValues = tvValues.getTable();

        final TableColumn columnSScope = new TableColumn(tblValues, SWT.NONE);
        columnSScope.setWidth(100);
        columnSScope.setText(Messages.ColumnLabel_Scope);

        final TableColumn columnValue = new TableColumn(tblValues, SWT.NONE);
        columnValue.setWidth(200);
        columnValue.setText(Messages.ColumnLabel_Value);

        tblValues.setLinesVisible(true);
        tblValues.setHeaderVisible(true);

        tvValues.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        tvValues.setLabelProvider(new ValuesLabelProvider());
        tvValues.setContentProvider(new ValuesContentProvider());

        CellEditor[] editors = new CellEditor[4];
        editors[0] = new TextCellEditor(tvValues.getTable());
        editors[1] = new TextCellEditor(tvValues.getTable());

        tvValues.setColumnProperties(PROPS);
        tvValues.setCellEditors(editors);
        tvValues.setCellModifier(new ScopedValuesModifier(tvValues));
    }

    protected void performCreateVariable(String name, String description) {

        Category category = getSelectedCategory();
        if (category == null) {
            return;
        }

        Variable variable = new Variable(name, description);
        category.addVariable(variable);

        setVariables(category.getVariableNames());

        selectVariable(name);
    }

    protected void performDeleteVariable(String name) {

        Category category = getSelectedCategory();
        if (category == null) {
            return;
        }

        int newIndex = -1;

        for (int i = 0; i < listVariablesSelector.getItemCount(); i++) {
            String currentName = listVariablesSelector.getItem(i);
            if (currentName.equals(name)) {
                newIndex = i;
                break;
            }
        }

        category.removeVariable(name);

        setVariables(category.getVariableNames());

        selectVariable(newIndex);
    }

    private void selectVariable(String name) {

        for (int i = 0; i < listVariablesSelector.getItemCount(); i++) {
            String currentName = listVariablesSelector.getItem(i);
            if (currentName.equals(name)) {
                selectVariable(i);
                break;
            }
        }

        setControlsEnablement();
    }

    private void selectVariable(int index) {

        listVariablesSelector.setFocus();

        if (index <= 0 && listVariablesSelector.getItemCount() > 0) {
            listVariablesSelector.select(1);
        } else if (index >= listVariablesSelector.getItemCount()) {
            if (listVariablesSelector.getItemCount() >= 2) {
                listVariablesSelector.select(1);
            } else {
                listVariablesSelector.select(0);
            }
        } else {
            listVariablesSelector.select(index);
        }

        updateVariableTextFields(getSelectedVariable());

        setControlsEnablement();
    }

    private Layout createLayoutNoMargins(int columns) {
        GridLayout gl = new GridLayout(columns, false);
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        return gl;
    }

    private Label createLabel(Composite parent, String text) {
        return createLabel(parent, text, 1);
    }

    private void setVariables(String[] variables) {

        listVariablesSelector.setItems(variables);
        listVariablesSelector.add(NEW_VARIABLE, 0);
    }

    private Category getSelectedCategory() {

        int categoryIndex = cbCategory.getSelectionIndex();
        if (categoryIndex >= 0) {
            return categories[categoryIndex];
        }

        return null;
    }

    private Variable getSelectedVariable() {

        Category category = getSelectedCategory();
        if (category == null) {
            return null;
        }

        int variableIndex = listVariablesSelector.getSelectionIndex();
        if (variableIndex < 0) {
            return null;
        }

        String name = getSelectedVariableName();

        return category.getVariable(name);
    }

    private String getSelectedVariableName() {

        int variableIndex = listVariablesSelector.getSelectionIndex();
        if (variableIndex < 0) {
            return null;
        }

        return listVariablesSelector.getItem(variableIndex);
    }

    private void performCategoryChangedEvent() {

        int index = cbCategory.getSelectionIndex();
        if (index < 0) {
            setVariables(new String[0]);
            return;
        }

        Category category = categories[index];
        setVariables(category.getVariableNames());

        // if (listVariablesSelector.getItemCount() > 0) {
        // String variableName = listVariablesSelector.getItem(0);
        // if (NEW_VARIABLE.equalsIgnoreCase(variableName)) {
        // listVariablesSelector.select(1);
        // } else {
        // listVariablesSelector.select(0);
        // }
        // }
        selectVariable(1);

        performVariableChangedEvent();
    }

    private void performVariableChangedEvent() {

        int categoryIndex = cbCategory.getSelectionIndex();
        if (categoryIndex < 0) {
            tvValues.setInput(null);
            return;
        }

        Category category = categories[categoryIndex];

        String name = getSelectedVariableName();
        if (name == null) {
            tvValues.setInput(null);
            return;
        } else {

            Variable variable = category.getVariable(name);
            tvValues.setInput(variable);
            updateVariableTextFields(variable);

            if (tvValues.getTable().getItemCount() > 0) {
                tvValues.getTable().select(0);
            }

        }

        setControlsEnablement();
    }

    private void updateVariableTextFields(Variable variable) {

        if (variable == null) {
            txtName.setText(""); //$NON-NLS-1$
            txtDescription.setText(""); //$NON-NLS-1$
            return;
        }

        txtName.setText(variable.getName());
        txtDescription.setText(variable.getDescription());
    }

    @Override
    protected void performApply() {
        super.performApply();
    }

    @Override
    protected void performDefaults() {
        setScreenToDefaultValues();
        super.performDefaults();
    }

    @Override
    public boolean performOk() {

        setStoreToValues();
        return super.performOk();
    }

    protected void setStoreToValues() {

        Preferences preferences = getPreferences();

        preferences.setCetegories(categories);
    }

    protected void setScreenToValues() {

        Preferences preferences = getPreferences();
        setCategories(preferences.getCategories());

        checkAllValues();
        setControlsEnablement();
    }

    protected void setScreenToDefaultValues() {

        Preferences preferences = getPreferences();
        setCategories(preferences.getDefaultCategories());

        checkAllValues();
        setControlsEnablement();
    }

    private void setCategories(Category[] categories) {

        this.categories = categories;

        for (Category category : this.categories) {
            cbCategory.add(category.getName());
        }

        if (cbCategory.getItemCount() > 0) {
            cbCategory.select(0);
            performCategoryChangedEvent();
        }
    }

    private boolean checkAllValues() {

        return clearError();
    }

    private void setControlsEnablement() {

        String name = getSelectedVariableName();

        if (NEW_VARIABLE.equalsIgnoreCase(name)) {

            lblNewEdit.setText(Messages.Label_New_variable);
            txtName.setEditable(true);

            if (validateNewVariable()) {
                btnCreate.setEnabled(true);
            } else {
                btnCreate.setEnabled(false);
            }

        } else {
            lblNewEdit.setText(Messages.Label_Edit_variable);
            txtName.setEditable(false);
            btnCreate.setEnabled(false);
        }
    }

    private boolean validateNewVariable() {

        clearError();

        Category category = getSelectedCategory();
        if (category == null) {
            return false;
        }

        if (!hasName()) {
            setError("Name is missing.");
            return false;
        }

        if (category.hasVariable(txtName.getText())) {
            setError("Variable already exists.");
            return false;
        }

        if (!hasDescription()) {
            setError("Description is missing");
            return false;
        }

        return true;
    }

    private boolean hasName() {

        if (txtName.getText().trim().length() > 0) {
            return true;
        }

        return false;
    }

    private boolean hasDescription() {

        if (txtDescription.getText().trim().length() > 0) {
            return true;
        }

        return false;
    }

    private boolean setError(String message) {
        setErrorMessage(message);
        setValid(false);
        return false;
    }

    private boolean clearError() {
        setErrorMessage(null);
        setValid(true);
        return true;
    }

    private Label createLabel(Composite parent, String text, int columns) {

        Label label = new Label(parent, SWT.NONE);
        label.setLayoutData(createLabelLayoutData(columns));
        label.setText(text);

        return label;
    }

    private GridData createLabelLayoutData(int columns) {

        GridData gd;
        if (columns == 1) {
            gd = new GridData(SWT.DEFAULT, SWT.DEFAULT, false, false, columns, 1);
        } else {
            gd = new GridData(SWT.FILL, SWT.DEFAULT, true, false, columns, 1);
        }
        return gd;
    }

    private GridData createLayoutData() {
        return new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
    }

    private GridData createLayoutData(int horizontalSpan) {
        return new GridData(SWT.FILL, SWT.BEGINNING, true, false, horizontalSpan, 1);
    }

    private Preferences getPreferences() {
        return Preferences.getInstance();
    }

    private class ValuesContentProvider implements IStructuredContentProvider {

        private Variable variable = null;
        private java.util.List<ScopedValue> scopedValues = new LinkedList<ScopedValue>();

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

            if (newInput instanceof Variable) {
                variable = (Variable)newInput;
                scopedValues.clear();
                for (ScopedValue scopedValue : variable.getScopedValues()) {
                    scopedValues.add(scopedValue);
                }
            } else {
                variable = null;
                scopedValues.clear();
            }

            updateInput();
        }

        public Object[] getElements(Object element) {

            return scopedValues.toArray(new ScopedValue[scopedValues.size()]);
        }

        public void updateInput() {

            if (variable != null) {

                // Check presence of <new> item
                boolean hasNewItem = hasNewItem(scopedValues);

                // Remove empty and <new> items
                java.util.List<ScopedValue> itemsToRemove = getRemoveableItems(scopedValues);
                scopedValues.removeAll(itemsToRemove);

                // Update variable
                boolean isUpdated = false;
                if (canUpdateVariable()) {
                    variable.clear();
                    for (ScopedValue scopedValue : scopedValues) {
                        variable.addScopedValue(scopedValue);
                    }
                    isUpdated = true;
                }

                // Sort items
                if (isUpdated) {
                    scopedValues.clear();
                    ScopedValue[] values = variable.getScopedValues();
                    Arrays.sort(values);
                    for (ScopedValue scopedValue : values) {
                        scopedValues.add(scopedValue);
                    }
                }

                // Add <new> item
                if (hasNewItem || isUpdated) {
                    scopedValues.add(0, new ScopedValue(NEW_SCOPED_VALUE, ""));
                }
            }
        }

        private boolean hasNewItem(java.util.List<ScopedValue> scopedValues2) {

            for (ScopedValue scopedValue : scopedValues) {
                if (NEW_SCOPED_VALUE.equalsIgnoreCase(scopedValue.getScope())) {
                    return true;
                }
            }

            return false;
        }

        private java.util.List<ScopedValue> getRemoveableItems(java.util.List<ScopedValue> scopedValues) {

            java.util.List<ScopedValue> itemsToRemove = new ArrayList<ScopedValue>();

            for (ScopedValue scopedValue : scopedValues) {
                if (NEW_SCOPED_VALUE.equalsIgnoreCase(scopedValue.getScope()) || scopedValue.getScope().length() == 0
                    && scopedValue.getValue().length() == 0) {
                    itemsToRemove.add(scopedValue);
                }
            }

            return itemsToRemove;
        }

        private boolean canUpdateVariable() {

            boolean canUpdateVariable = true;
            for (ScopedValue scopedValue : scopedValues) {
                if (!isValidScopedValue(scopedValue)) {
                    canUpdateVariable = false;
                }
            }

            return canUpdateVariable;
        }

        private boolean isValidScopedValue(ScopedValue scopedValue) {

            if (scopedValue.getScope().length() == 0 || scopedValue.getValue().length() == 0) {
                return false;
            }

            return true;
        }
    }

    private class ValuesLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object arg0, int arg1) {
            return null;
        }

        public String getColumnText(Object element, int column) {

            if (element instanceof ScopedValue) {
                ScopedValue scopedValue = (ScopedValue)element;
                switch (column) {
                case 0:
                    return scopedValue.getScope();
                case 1:
                    return scopedValue.getValue();
                default:
                    break;
                }
            }

            return null;
        }
    }

    private class ScopedValuesModifier implements ICellModifier {

        private TableViewer viewer;

        public ScopedValuesModifier(TableViewer viewer) {
            this.viewer = viewer;
        }

        public boolean canModify(Object element, String property) {
            return true;
        }

        public Object getValue(Object element, String property) {

            if (element instanceof ScopedValue) {
                ScopedValue scopedValue = (ScopedValue)element;

                if (VariablesPreferencePage.SCOPE.equals(property)) {
                    return scopedValue.getScope();
                } else if (VariablesPreferencePage.VALUE.equals(property)) {
                    return scopedValue.getValue();
                }
            }

            return null;
        }

        public void modify(Object element, String property, Object value) {

            if (element instanceof Item) {
                Item item = (Item)element;
                ScopedValue scopedValue = (ScopedValue)item.getData();

                if (VariablesPreferencePage.SCOPE.equals(property)) {
                    scopedValue.setScope((String)value);
                } else if (VariablesPreferencePage.VALUE.equals(property)) {
                    scopedValue.setValue((String)value);
                }

                ValuesContentProvider contentProvider = (ValuesContentProvider)viewer.getContentProvider();

                contentProvider.updateInput();

                viewer.refresh();
            }
        }
    }

    /**
     * Class that implements the context menu for the table rows.
     */
    private class VariablesContextMenu extends MenuAdapter {

        private Menu menu;
        private MenuItem menuItemDelete;
        private String[] selectedVariableNames;

        public VariablesContextMenu(Menu menu) {
            this.menu = menu;
        }

        @Override
        public void menuShown(MenuEvent event) {
            destroyMenuItems();
            createMenuItems();
        }

        private Menu getMenu() {
            return menu;
        }

        private void destroyMenuItems() {
            if (!((menuItemDelete == null) || (menuItemDelete.isDisposed()))) {
                menuItemDelete.dispose();
            }
        }

        private void createMenuItems() {

            selectedVariableNames = listVariablesSelector.getSelection();
            for (String variableName : selectedVariableNames) {
                if (NEW_VARIABLE.equals(variableName)) {
                    return;
                }
            }

            createMenuItemCopySelected();
        }

        private void createMenuItemCopySelected() {

            menuItemDelete = new MenuItem(getMenu(), SWT.NONE);
            menuItemDelete.setText("Delete");
            menuItemDelete.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    for (String variableName : selectedVariableNames) {
                        performDeleteVariable(variableName);
                    }
                }
            });
        }
    }
}