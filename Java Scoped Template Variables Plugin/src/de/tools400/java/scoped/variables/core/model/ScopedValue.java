/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.model;

public class ScopedValue implements Comparable<ScopedValue> {

    private String scope;
    private String value;

    public ScopedValue() {
        this("*", "");
    }

    public ScopedValue(String scope, String value) {
        setScope(scope);
        setValue(value);
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value.trim();
    }

    public int compareTo(ScopedValue element) {

        if (element == null) {
            return 1;
        } else if (scope == null && element.getScope() == null) {
            return 0;
        } else if (element.getScope() == null) {
            return 1;
        } else if (scope == null) {
            return -1;
        } else {
            return scope.compareTo(element.getScope());
        }
    }

    @Override
    public String toString() {
        return scope + "=" + value; //$NON-NLS-1$
    }
}
