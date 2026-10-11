/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.resolver;

import java.lang.reflect.Method;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariableResolver;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;

import de.tools400.java.scoped.variables.core.JavaScopedTemplateVariablesPlugin;
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

        final IJavaProject javaProject = getJavaProject(pContext);
        if (javaProject == null) {
            return null;
        }

        // final IPluginModelBase pluginModelBase =
        // PluginRegistry.findModel(javaProject.getProject());

        // String v = pContext.getVariable("package_name");

        final IPluginModelBase pluginModelBase = PDECore.getDefault().getModelManager().findModel(javaProject.getProject());
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

    /**
     * Returns the Java project of a code template context.
     * <p>
     * Uses reflection, because class <code>CodeTemplateContext</code> has been
     * moved from package <code>org.eclipse.jdt.internal.corext.template.java</code>
     * (bundle <code>org.eclipse.jdt.ui</code>) to package
     * <code>org.eclipse.jdt.internal.core.manipulation</code> (bundle
     * <code>org.eclipse.jdt.core.manipulation</code>) with Eclipse 4.10.
     */
    private IJavaProject getJavaProject(TemplateContext context) {

        try {
            Method method = context.getClass().getMethod("getJavaProject"); //$NON-NLS-1$
            return (IJavaProject)method.invoke(context);
        } catch (Exception e) {
            JavaScopedTemplateVariablesPlugin.logError("*** Could not get Java project from template context ***", e); //$NON-NLS-1$
            return null;
        }
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