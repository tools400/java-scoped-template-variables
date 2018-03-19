/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Category {

    private String id;
    private String name;
    private Map<String, Variable> variables;

    public Category() {
        this(null, null);
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
        this.variables = new HashMap<String, Variable>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Variable[] getVariables() {
        return variables.values().toArray(new Variable[variables.size()]);
    }

    public Variable getVariable(String name) {
        return variables.get(name);
    }

    public String[] getVariableNames() {

        Variable[] variables = getVariables();
        String[] names = new String[variables.length];

        for (int i = 0; i < names.length; i++) {
            names[i] = variables[i].getName();
        }

        Arrays.sort(names);

        return names;
    }

    public void setVariables(Map<String, Variable> variables) {
        this.variables = variables;
    }

    public void addVariable(Variable variable) {

        if (hasVariable(variable.getName())) {
            return;
        }

        variables.put(variable.getName(), variable);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }
}
