/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.preferences;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.json.JSONException;

import de.tools400.java.scoped.variables.core.JavaScopedTemplateVariablesPlugin;
import de.tools400.java.scoped.variables.core.model.Category;
import de.tools400.java.scoped.variables.core.model.ICategory;
import de.tools400.java.scoped.variables.core.model.JsonVariableAdapter;
import de.tools400.java.scoped.variables.core.model.Variable;

/**
 * Class to manage access to the preferences of the plugin.
 * <p>
 * Eclipse stores the preferences as <i>diffs</i> to their default values in
 * directory
 * <code>[workspace]\.metadata\.plugins\org.eclipse.core.runtime\.settings\</code>.
 * 
 * @author Thomas Raddatz
 */
public final class Preferences {

    /**
     * The instance of this Singleton class.
     */
    private static Preferences instance;

    /**
     * Global preferences of the plugin.
     */
    private IPreferenceStore preferenceStore;

    private JsonVariableAdapter jsonVariableAdapter;

    /*
     * Preferences keys:
     */

    private static final String DOMAIN = JavaScopedTemplateVariablesPlugin.PLUGIN_ID + "."; //$NON-NLS-1$
    private static final String CATEGORIES = DOMAIN + "CATEGORIES"; //$NON-NLS-1$
    private static final String CATEGORY = DOMAIN + "CATEGORY."; //$NON-NLS-1$

    private static final String EMPTY_JSON_ARRAY = "{}"; //$NON-NLS-1$
    private static final String DELIMITER_CATEGORIES = ";";
    private static final String DELIMITER_KEY_VALUE = "=";

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private Preferences() {
    }

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
            instance.jsonVariableAdapter = new JsonVariableAdapter(); //$NON-NLS-1$
        }
        return instance;
    }

    private IPreferenceStore getPreferenceStore() {

        if (preferenceStore == null) {
            JavaScopedTemplateVariablesPlugin plugin = JavaScopedTemplateVariablesPlugin.getDefault();
            if (plugin != null) {
                preferenceStore = plugin.getPreferenceStore();
            }
        }

        return preferenceStore;
    }

    /**
     * Thread-safe method that disposes the instance of this Singleton class.
     * <p>
     * This method is intended to be call from
     * {@link org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)}
     * to free the reference to itself.
     */
    public static void dispose() {
        if (instance != null) {
            instance = null;
        }
    }

    /*
     * Preferences: GETTER
     */

    public Category getCategory(String id) {

        String jsonString = getPreferenceStore().getString(CATEGORY + id);

        try {
            return jsonVariableAdapter.deserializeCategory(jsonString);
        } catch (JSONException e) {
            JavaScopedTemplateVariablesPlugin.logError("*** Could not get 'Variables' from preference store ***", e); //$NON-NLS-1$
        }

        return null;
    }

    public Category[] getCategories() {

        List<Category> categories = new LinkedList<Category>();

        String categoryIds = getPreferenceStore().getString(CATEGORIES);
        for (String categoryId : categoryIds.split(DELIMITER_CATEGORIES)) { //$NON-NLS-1$
            String[] parts = categoryId.split(DELIMITER_KEY_VALUE); //$NON-NLS-1$
            categories.add(getCategory(parts[0]));
        }

        return categories.toArray(new Category[categories.size()]);
    }

    /*
     * Preferences: SETTER
     */

    public void setCategory(Category category) {

        String jsonString;

        try {
            jsonString = jsonVariableAdapter.serializeCategory(category);
            getPreferenceStore().setValue(CATEGORY + category.getId(), jsonString);
        } catch (JSONException e) {
            JavaScopedTemplateVariablesPlugin.logError("*** Could not store 'Variables' in preference store ***", e); //$NON-NLS-1$
        }

        return;
    }

    public void setCetegories(Category[] categories) {

        for (Category category : categories) {
            setCategory(category);
        }
    }

    /*
     * Preferences: Default Initializer
     */

    public void initializeDefaultPreferences() {

        Category[] categories = getDefaultCategories();

        String jsonString;
        try {
            for (Category category : categories) {
                jsonString = jsonVariableAdapter.serializeCategory(category);
                getPreferenceStore().setDefault(CATEGORY + category.getId(), jsonString);
            }

        } catch (JSONException e) {
            jsonString = EMPTY_JSON_ARRAY;
        }

        StringBuilder buffer = new StringBuilder();
        for (Category category : categories) {
            if (buffer.length() > 0) {
                buffer.append(DELIMITER_CATEGORIES); //$NON-NLS-1$
            }
            buffer.append(category.getId());
            buffer.append(DELIMITER_KEY_VALUE); //$NON-NLS-1$
            buffer.append(category.getName());
        }

        getPreferenceStore().setDefault(CATEGORIES, buffer.toString());
    }

    /*
     * Preferences: Default Values
     */

    public Category[] getDefaultCategories() {

        List<Category> categories = new LinkedList<Category>();

        Category category;
        category = new Category(ICategory.PROJECT, "Project");

        Variable variable;
        variable = new Variable("project_team"); //$NON-NLS-1$
        variable.setDescription("Name of the development team"); //$NON-NLS-1$
        variable.addScopedValue("de.tools400.*", "Tools/400"); //$NON-NLS-1$
        variable.addScopedValue("biz.isphere.*", "iSphere Project Team"); //$NON-NLS-1$
        variable.addScopedValue("biz.rapidfire.*", "Rapid Fire Project Team"); //$NON-NLS-1$
        category.addVariable(variable);

        variable = new Variable("project_year"); //$NON-NLS-1$
        variable.setDescription("Year when the project has been started"); //$NON-NLS-1$
        variable.addScopedValue("biz.isphere.*", "2012"); //$NON-NLS-1$
        variable.addScopedValue("biz.rapidfire.*", "2017"); //$NON-NLS-1$
        category.addVariable(variable);

        categories.add(category);

        return categories.toArray(new Category[categories.size()]);
    }
}