/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.startup;

import java.util.Iterator;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import de.tools400.java.scoped.variables.core.JavaScopedTemplateVariablesPlugin;
import de.tools400.java.scoped.variables.core.model.Category;
import de.tools400.java.scoped.variables.core.model.ICategory;
import de.tools400.java.scoped.variables.core.model.Variable;
import de.tools400.java.scoped.variables.core.model.VariableStore;
import de.tools400.java.scoped.variables.core.resolver.ProjectScopedVariableResolver;

@SuppressWarnings("restriction")
public class RegisterProjectScopedVariableResolver implements IStartup {

    private static final String JAVA_PLUGIN_ID = JavaPlugin.getPluginId();

    private Category category;

    public RegisterProjectScopedVariableResolver() {
        category = VariableStore.getInstance().getCategory(ICategory.PROJECT);
    }

    public void earlyStartup() {

        // check if plug-in org.eclipse.jdt.ui is already active
        final Bundle bundle = Platform.getBundle(JAVA_PLUGIN_ID);
        if (bundle != null && bundle.getState() == Bundle.ACTIVE) {

            // register resolvers
            registerResolvers(category.getVariables());
        } else {

            // register listener to get informed, when plug-in becomes active
            final BundleContext bundleContext = JavaScopedTemplateVariablesPlugin.getDefault().getBundleContext();
            bundleContext.addBundleListener(new BundleListener() {
                public void bundleChanged(final BundleEvent pEvent) {
                    final Bundle bundle2 = pEvent.getBundle();
                    if (!bundle2.getSymbolicName().equals(JAVA_PLUGIN_ID)) {
                        return;
                    }
                    if (bundle2.getState() == Bundle.ACTIVE) {
                        registerResolvers(category.getVariables());
                        bundleContext.removeBundleListener(this);
                    }
                }
            });
        }
    }

    /**
     * Internal method to register resolvers with all context types.
     */
    private void registerResolvers(Variable[] variables) {

        final ContextTypeRegistry codeTemplateContextRegistry = JavaPlugin.getDefault().getCodeTemplateContextRegistry();
        final Iterator<?> ctIter = codeTemplateContextRegistry.contextTypes();

        while (ctIter.hasNext()) {
            final TemplateContextType contextType = (TemplateContextType)ctIter.next();
            for (Variable variable : variables) {
                contextType.addResolver(new ProjectScopedVariableResolver(variable.getName(), variable.getDescription()));
            }
        }
    }
}
