/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.test;

import de.tools400.java.scoped.variables.core.model.Category;
import de.tools400.java.scoped.variables.core.model.JsonVariableAdapter;
import de.tools400.java.scoped.variables.core.preferences.Preferences;

public class Test {

    public static void main(String[] args) throws Exception {
        Test main = new Test();
        main.run(args);
    }

    private void run(String[] args) throws Exception {

        Category[] categories = Preferences.getInstance().getDefaultCategories();

        JsonVariableAdapter mapper = new JsonVariableAdapter();
        String jsonString1 = mapper.serializeCategory(categories[0], 0);

        System.out.println();
        System.out.println(jsonString1);

        Category parsedCategory = mapper.deserializeCategory(jsonString1);
        String jsonString2 = mapper.serializeCategory(parsedCategory, 0);

        System.out.println();
        System.out.println(jsonString2);

        System.out.println();
        if (jsonString1.equals(jsonString2)) {
            System.out.println("** Ready **");
        } else {
            System.out.println("** E R R O R **");
        }
    }
}