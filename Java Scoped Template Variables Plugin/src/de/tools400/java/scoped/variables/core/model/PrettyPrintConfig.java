/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.model;

public class PrettyPrintConfig {

    int indention;
    boolean isPrettyPrint;

    public PrettyPrintConfig() {
        this.indention = 3;
        this.isPrettyPrint = false;
    }

    public int getIndention() {
        return indention;
    }

    public void setIndention(int indention) {
        this.indention = indention;
    }

    public boolean isPrettyPrint() {
        return isPrettyPrint;
    }

    public void setPrettyPrint(boolean enabled) {
        this.isPrettyPrint = enabled;
    }
}
