/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.resolver;

import org.eclipse.jdt.internal.corext.template.java.CodeTemplateContext;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariableResolver;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;

import de.tools400.java.scoped.variables.core.model.Category;
import de.tools400.java.scoped.variables.core.model.ICategory;
import de.tools400.java.scoped.variables.core.model.ScopedValue;
import de.tools400.java.scoped.variables.core.model.Variable;
import de.tools400.java.scoped.variables.core.model.VariableStore;

@SuppressWarnings("restriction")
public class ProjectScopedVariableResolver extends TemplateVariableResolver {

    /**
     * Constructs a new <code>BundleIdResolver</code>.
     */
    public ProjectScopedVariableResolver(String name, String description) {
        super(name, description);
    }

    /**
     * {@inheritDoc}
     * 
     * @see TemplateVariableResolver#resolve(org.eclipse.jface.text.templates.TemplateContext)
     */
    @Override
    protected String resolve(final TemplateContext pContext) {

        final CodeTemplateContext context = (CodeTemplateContext)pContext;
        // final IPluginModelBase pluginModelBase =
        // PluginRegistry.findModel(context.getJavaProject().getProject());
        final IPluginModelBase pluginModelBase = PDECore.getDefault().getModelManager().findModel(context.getJavaProject().getProject());
        if (pluginModelBase == null) {
            return null;
        }

        String pluginId = pluginModelBase.getBundleDescription().getSymbolicName();

        ScopedValue scopedValue = resolveValue(pluginId);
        if (scopedValue == null) {
            return null;
        }

        return scopedValue.getValue();
    }

    private ScopedValue resolveValue(String pluginId) {

        Category category = VariableStore.getInstance().getCategory(ICategory.PROJECT);
        if (category == null) {
            return null;
        }

        Variable variable = category.getVariable(getName());
        if (variable == null) {
            return null;
        }

        return variable.getScopedValue(pluginId);
    }

    private String getName() {
        return getType();
    }
}