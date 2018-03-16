/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "de.tools400.java.scoped.variables.core.messages";

    public static String Label_Category;
    public static String Label_Variables;
    public static String Label_Name;
    public static String Label_Description;
    public static String Label_New_variable;
    public static String Label_Edit_variable;

    public static String ColumnLabel_Scope;
    public static String ColumnLabel_Value;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

}
