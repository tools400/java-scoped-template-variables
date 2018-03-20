/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import de.tools400.java.scoped.variables.core.JavaScopedTemplateVariablesPlugin;
import de.tools400.java.scoped.variables.core.preferences.Preferences;

public final class VariableStore implements IPropertyChangeListener {

    /**
     * The instance of this Singleton class.
     */
    private static VariableStore instance;

    private Map<String, Category> categories;

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private VariableStore() {
        initialize();
    }

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static VariableStore getInstance() {

        if (instance == null) {
            instance = new VariableStore();
            JavaScopedTemplateVariablesPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(instance);
        }

        return instance;
    }

    private void initialize() {

        categories = new HashMap<String, Category>();

        Category[] categories = Preferences.getInstance().getCategories();
        for (Category category : categories) {
            this.categories.put(category.getId(), category);
        }
    }

    public Category[] getCategories() {
        return categories.values().toArray(new Category[categories.size()]);
    }

    public Category getCategory(String id) {

        return categories.get(id);
    }

    /**
     * Thread-safe method that disposes the instance of this Singleton class.
     * <p>
     * This method is intended to be call from
     * {@link org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)}
     * to free the reference to itself.
     */
    public synchronized static void dispose() {

        if (instance != null) {
            JavaScopedTemplateVariablesPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(instance);
            instance = null;
        }
    }

    public void propertyChange(PropertyChangeEvent event) {

        if (Preferences.CATEGORIES.equals(event.getProperty())) {
            initialize();
        }
    }
}
