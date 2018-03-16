/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.model;

import java.util.Comparator;

public class VariableComparator implements Comparator<Variable> {

    public static int DESCENDING = -1;
    public static int ASCENDING = 0;

    private int orderBy;

    public VariableComparator(int orderBy) {

        if (orderBy != DESCENDING && orderBy != ASCENDING) {
            throw new IllegalArgumentException("Value of 'orderBy' must be one of [ASCENDING|DESCENDING]."); //$NON-NLS-1$
        }

        this.orderBy = orderBy;
    }

    public int compare(Variable variable, Variable element) {

        int result;

        if (variable == null && element == null) {
            result = 0;
        } else if (element == null) {
            result = 1;
        } else if (variable == null) {
            result = -1;
        } else {
            result = variable.compareTo(element);
        }

        if (orderBy == DESCENDING) {
            result = result * -1;
        }

        return result;
    }

}
