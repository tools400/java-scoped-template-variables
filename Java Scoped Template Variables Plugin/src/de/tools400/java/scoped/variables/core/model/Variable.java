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

public class Variable implements Comparable<Variable> {

    private static final String ASTERISK = "*";//$NON-NLS-1$

    private String name;
    private String description;
    private Map<String, ScopedValue> scopedValues;

    public Variable() {
        this(null, ""); //$NON-NLS-1$
    }

    public Variable(String name) {
        this(name, ""); //$NON-NLS-1$
    }

    public Variable(String name, String description) {
        this.name = name;
        this.description = description;
        clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ScopedValue[] getScopedValues() {
        return scopedValues.values().toArray(new ScopedValue[scopedValues.size()]);
    }

    public void setScopedValues(Map<String, ScopedValue> scopedValues) {
        this.scopedValues = scopedValues;
    }

    public ScopedValue getScopedValue(String scope) {

        ScopedValue[] scopedValues = this.scopedValues.values().toArray(new ScopedValue[this.scopedValues.size()]);
        Arrays.sort(scopedValues, new ScopedValueComparator(ScopedValueComparator.DESCENDING));

        for (ScopedValue scopedValue : scopedValues) {
            if (isMatchingScope(scopedValue.getScope(), scope)) {
                return scopedValue;
            }
        }

        return null;
    }

    /**
     * Returns <code>true</code>, if a given scope matches the scope of a scoped
     * value. A scope that ends with an asterisk matches all scopes that start
     * with the characters in front of the asterisk. Otherwise the scopes must
     * be equal. The comparison is not case sensitive.
     */
    private boolean isMatchingScope(String valueScope, String scope) {

        if (valueScope.endsWith(ASTERISK)) {
            int end = valueScope.indexOf(ASTERISK);
            return scope.regionMatches(true, 0, valueScope, 0, end);
        }

        return valueScope.equalsIgnoreCase(scope);
    }

    public void clear() {
        scopedValues = new HashMap<String, ScopedValue>();
    }

    public boolean hasScope(String scope) {

        if (scopedValues.containsKey(scope)) {
            return true;
        }

        return false;
    }

    public void addScopedValue(ScopedValue scopedValue) {

        if (hasScope(scopedValue.getScope())) {
            return;
        }

        scopedValues.put(scopedValue.getScope(), scopedValue);
    }

    public void addScopedValue(String scope, String value) {
        addScopedValue(new ScopedValue(scope, value));
    }

    public int compareTo(Variable element) {

        if (element == null) {
            return 1;
        } else if (name == null && element.getName() == null) {
            return 0;
        } else if (element.getName() == null) {
            return 1;
        } else if (name == null) {
            return -1;
        } else {
            return name.compareTo(element.getName());
        }
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();

        buffer.append(name);
        buffer.append("[");
        for (ScopedValue value : scopedValues.values()) {
            buffer.append(value.getScope());
            buffer.append("=");
            buffer.append(value.getValue());
            buffer.append(";");
        }
        buffer.append("]");

        return buffer.toString();
    }
}
