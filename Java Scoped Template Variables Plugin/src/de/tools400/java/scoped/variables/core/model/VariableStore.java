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

import de.tools400.java.scoped.variables.core.preferences.Preferences;

public final class VariableStore {

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
}
