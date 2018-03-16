/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.model;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonVariableAdapter {

    private static final String CATEGORY_ID = "id"; //$NON-NLS-1$
    private static final String CATEGORY_NAME = "name"; //$NON-NLS-1$
    private static final String CATEGORY_VARIABLES = "variables"; //$NON-NLS-1$

    private static final String VARIABLE_NAME = "name"; //$NON-NLS-1$
    private static final String VARIABLE_DESCRIPTION = "description"; //$NON-NLS-1$
    private static final String VARIABLE_SCOPED_VALUES = "scopedValues"; //$NON-NLS-1$

    private static final String SCOPED_VALUE_SCOPE = "scope"; //$NON-NLS-1$
    private static final String SCOPED_VALUE_VALUE = "value"; //$NON-NLS-1$

    private PrettyPrintConfig prettyPrintConfig;

    public JsonVariableAdapter() {
        this.prettyPrintConfig = new PrettyPrintConfig();
    }

    public boolean isPrettyPrint() {
        return prettyPrintConfig.isPrettyPrint;
    }

    public void setPrettyPrint(boolean enabled) {
        this.prettyPrintConfig.setPrettyPrint(enabled);
    }

    public int getIndention() {
        return prettyPrintConfig.getIndention();
    }

    public void setIndention(int indention) {
        this.prettyPrintConfig.setIndention(indention);
    }

    public String serializeCategory(Category category) throws JSONException {
        return serializeCategory(category, -1);
    }

    public String serializeCategory(Category category, int indention) throws JSONException {

        JSONObject jCategory = new JSONObject();
        jCategory.put(CATEGORY_ID, category.getId());
        jCategory.put(CATEGORY_NAME, category.getName());
        jCategory.put(CATEGORY_VARIABLES, serializeVariables(category.getVariables()));

        PrettyPrintConfig savedPrettyPrintConfig = prettyPrintConfig;

        try {

            setIndention(indention);

            if (getIndention() <= 0) {
                setPrettyPrint(false);
            } else {
                setPrettyPrint(true);
            }

            if (isPrettyPrint()) {
                return jCategory.toString(getIndention());
            } else {
                return jCategory.toString();
            }

        } finally {
            this.prettyPrintConfig = savedPrettyPrintConfig;
        }
    }

    private JSONArray serializeVariables(Variable[] variables) throws JSONException {

        Arrays.sort(variables, new VariableComparator(VariableComparator.DESCENDING));

        JSONArray jVariables = new JSONArray();
        for (Variable tVariable : variables) {
            JSONObject jScopedValue = serializeVariable(tVariable);
            jVariables.put(jScopedValue);
        }

        return jVariables;
    }

    private JSONObject serializeVariable(Variable variable) throws JSONException {

        JSONObject jVariable = new JSONObject();
        jVariable.put(VARIABLE_NAME, variable.getName());
        jVariable.put(VARIABLE_DESCRIPTION, variable.getDescription());
        jVariable.put(VARIABLE_SCOPED_VALUES, serializeScopedValues(variable.getScopedValues()));

        return jVariable;
    }

    private JSONArray serializeScopedValues(ScopedValue[] scopedValues) throws JSONException {

        Arrays.sort(scopedValues, new ScopedValueComparator(ScopedValueComparator.DESCENDING));

        JSONArray jScopedValues = new JSONArray();
        for (ScopedValue tScopedValue : scopedValues) {
            JSONObject jScopedValue = serializeScopedValue(tScopedValue);
            jScopedValues.put(jScopedValue);
        }

        return jScopedValues;
    }

    private JSONObject serializeScopedValue(ScopedValue scopedValue) throws JSONException {

        JSONObject jScopedValue = new JSONObject();
        jScopedValue.put(SCOPED_VALUE_SCOPE, scopedValue.getScope());
        jScopedValue.put(SCOPED_VALUE_VALUE, scopedValue.getValue());

        return jScopedValue;
    }

    public Category deserializeCategory(String jsonString) throws JSONException {
        return deserializeCategory(new JSONObject(jsonString));
    }

    private Category deserializeCategory(JSONObject jCategory) throws JSONException {

        String id = jCategory.getString(CATEGORY_ID);
        String name = jCategory.getString(CATEGORY_NAME);
        Category category = new Category(id, name);
        JSONArray jVariables = (JSONArray)jCategory.get(CATEGORY_VARIABLES);
        deserializeVariables(category, jVariables);

        return category;
    }

    private void deserializeVariables(Category category, JSONArray jVariables) throws JSONException {

        for (int s = 0; s < jVariables.length(); s++) {
            JSONObject jVariable = jVariables.getJSONObject(s);
            Variable variable = deserializeVariable(jVariable);
            category.addVariable(variable);
        }
    }

    private Variable deserializeVariable(JSONObject jVariable) throws JSONException {

        String name = jVariable.getString(VARIABLE_NAME);
        String description = jVariable.getString(VARIABLE_DESCRIPTION);
        Variable variable = new Variable(name, description);
        JSONArray jScopedValues = (JSONArray)jVariable.get(VARIABLE_SCOPED_VALUES);
        deserializeScopedValues(variable, jScopedValues);

        return variable;
    }

    private void deserializeScopedValues(Variable variable, JSONArray jScopedValues) throws JSONException {

        for (int s = 0; s < jScopedValues.length(); s++) {
            JSONObject jScopedValue = jScopedValues.getJSONObject(s);
            ScopedValue scopedValue = deserializeScopedValue(jScopedValue);
            variable.addScopedValue(scopedValue);
        }
    }

    private ScopedValue deserializeScopedValue(JSONObject jScopedValue) throws JSONException {

        String scope = jScopedValue.getString(SCOPED_VALUE_SCOPE);
        String value = jScopedValue.getString(SCOPED_VALUE_VALUE);
        ScopedValue scopedValue = new ScopedValue(scope, value);

        return scopedValue;
    }
}
