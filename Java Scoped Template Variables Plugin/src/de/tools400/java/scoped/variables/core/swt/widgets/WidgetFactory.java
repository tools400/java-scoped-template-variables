/*******************************************************************************
 * Copyright (c) 2017-2017 Tools/400
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package de.tools400.java.scoped.variables.core.swt.widgets;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import de.tools400.java.scoped.variables.core.swt.widgets.listener.SelectAllFocusListener;

/**
 * Factory for creating SWT widgets.
 * 
 * @author Thomas Raddatz
 */
public final class WidgetFactory {

    /**
     * The instance of this Singleton class.
     */
    private static WidgetFactory instance;

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private WidgetFactory() {
    }

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static WidgetFactory getInstance() {
        if (instance == null) {
            instance = new WidgetFactory();
        }
        return instance;
    }

    /**
     * Produces a color selector control.
     * 
     * @param parent - parent composite
     * @return color selector
     */
    public static ColorSelector createColorSelector(Composite parent) {
        return new ColorSelector(parent);
    }

    /**
     * Produces a separator.
     * 
     * @param parent - parent composite
     * @return separator
     */
    public static Label createSeparator(Composite parent) {
        return createSeparator(parent, 1);
    }

    /**
     * Produces a separator, spanning multiple columns.
     * 
     * @param parent - parent composite
     * @param span - number of columns to span
     * @return separator
     */
    public static Label createSeparator(Composite parent, int span) {

        Label separator = WidgetFactory.getInstance().produceSeparator(parent, SWT.SEPARATOR | SWT.HORIZONTAL);

        return separator;
    }

    /**
     * Produces a single line text field with a border.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct
     * @return text field
     */
    public static Text createText(Composite parent, int style) {
        return WidgetFactory.getInstance().produceText(parent, style, true);
    }

    /**
     * Produces a read-only single line text field.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct
     * @return read-only text field
     */
    public static Text createReadOnlyText(Composite parent, int style) {

        Text text = WidgetFactory.getInstance().produceText(parent, style, false);
        text.setEditable(false);

        return text;
    }

    /**
     * Produces a password field with a border.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct
     * @return password field
     */
    public static Text createPassword(Composite parent, int style) {
        return WidgetFactory.getInstance().produceText(parent, style | SWT.PASSWORD, true);
    }

    /**
     * Produces a label with selectable text.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct
     * @return label with selectable text
     */
    public static Text createSelectableLabel(Composite parent, int style) {

        Text text = new Text(parent, style);
        text.setEditable(false);

        return text;
    }

    /**
     * Produces a combo field.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct
     * @return combo field
     */
    public static Combo createCombo(Composite parent, int style) {
        return WidgetFactory.getInstance().produceComboField(parent, style);
    }

    /**
     * Produces a read-only combo field.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct
     * @return read-only combo field
     */
    public static Combo createReadOnlyCombo(Composite parent, int style) {
        return WidgetFactory.getInstance().produceComboField(parent, SWT.READ_ONLY | style);
    }

    /**
     * Produces a spinner field.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct (default: SWT.BORDER)
     * @return spinner field
     */
    public static Spinner createSpinner(Composite parent, int style) {
        return WidgetFactory.getInstance().produceSpinnerField(parent, style);
    }

    /**
     * Produces a read-only spinner field.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct (default: SWT.BORDER)
     * @return read-only spinner field
     */
    public static Spinner createReadOnlySpinner(Composite parent, int style) {
        return WidgetFactory.getInstance().produceSpinnerField(parent, SWT.READ_ONLY | style);
    }

    /**
     * Produces a checkbox field.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct
     * @return checkbox field
     */
    public static Button createCheckbox(Composite parent, int style) {
        return WidgetFactory.getInstance().produceCheckboxField(parent, style);
    }

    /**
     * Produces a read-only checkbox field.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct
     * @return read-only checkbox field
     */
    public static Button createReadOnlyCheckbox(Composite parent, int style) {

        Button checkBox = WidgetFactory.createCheckbox(parent, style);
        checkBox.setEnabled(false);

        return checkBox;
    }

    /**
     * Produces a push button.
     * 
     * @param parent - parent composite
     * @return push button
     */
    public static Button createPushButton(Composite parent) {
        return WidgetFactory.getInstance().producePushButton(parent);
    }

    /**
     * Produces a push button with a label.
     * 
     * @param parent - parent composite
     * @param string - button label
     * @return push button
     */
    public static Button createPushButton(Composite parent, String label) {
        Button button = WidgetFactory.getInstance().producePushButton(parent);
        button.setText(label);
        return button;
    }

    /**
     * Produces a read-only push button field.
     * 
     * @param parent - parent composite
     * @return read-only push button field
     */
    public static Button createReadOnlyPushButton(Composite parent) {

        Button pushButton = WidgetFactory.createPushButton(parent);
        pushButton.setEnabled(false);

        return pushButton;
    }

    /**
     * Produces a toggle button.
     * 
     * @param parent - parent composite
     * @return toggle button
     */
    public static Button createToggleButton(Composite parent) {
        return WidgetFactory.getInstance().produceToggleButton(parent);
    }

    /**
     * Produces a toggle button with a label.
     * 
     * @param parent - parent composite
     * @param style - additional style options, such as {@link SWT#FLAT}
     * @return toggle button
     */
    public static Button createToggleButton(Composite parent, int style) {
        Button button = WidgetFactory.getInstance().produceToggleButton(parent);
        return button;
    }

    /**
     * Produces a toggle button with a label.
     * 
     * @param parent - parent composite
     * @param string - button label
     * @return toggle button
     */
    public static Button createToggleButton(Composite parent, String label) {
        Button button = WidgetFactory.getInstance().produceToggleButton(parent);
        button.setText(label);
        return button;
    }

    /**
     * Produces a toggle button with a label.
     * 
     * @param parent - parent composite
     * @param string - button label
     * @param style - additional style options, such as {@link SWT#FLAT}
     * @return toggle button
     */
    public static Button createToggleButton(Composite parent, String label, int style) {
        Button button = WidgetFactory.getInstance().produceToggleButton(parent);
        button.setText(label);
        return button;
    }

    /**
     * Produces a radio button field.
     * 
     * @param parent - parent composite
     * @return radio button field
     */
    public static Button createRadioButton(Composite parent) {
        return WidgetFactory.getInstance().produceRadioButton(parent);
    }

    /**
     * Produces a read-only radio button field.
     * 
     * @param parent - parent composite
     * @return read-only radio button field
     */
    public static Button createReadOnlyRadioButton(Composite parent) {

        Button radioButton = WidgetFactory.createRadioButton(parent);
        radioButton.setEnabled(false);

        return radioButton;
    }

    /*
     * Private worker procedures, doing the actual work.
     */
    private Text produceText(Composite parent, int style, boolean autoSelect) {

        Text text = new Text(parent, style | SWT.BORDER);
        if (autoSelect) {
            text.addFocusListener(new SelectAllFocusListener());
        }

        return text;
    }

    private Combo produceComboField(Composite parent, int style) {

        Combo combo = new Combo(parent, style | SWT.DROP_DOWN);

        return combo;
    }

    private Spinner produceSpinnerField(Composite parent, int style) {

        Spinner spinner = new Spinner(parent, style);

        return spinner;
    }

    private Button produceCheckboxField(Composite parent, int style) {

        Button checkBox = new Button(parent, SWT.CHECK | style);

        return checkBox;
    }

    private Button producePushButton(Composite parent) {

        Button pushButton = new Button(parent, SWT.PUSH);

        return pushButton;
    }

    private Button produceToggleButton(Composite parent) {

        Button pushButton = new Button(parent, SWT.TOGGLE);

        return pushButton;
    }

    private Button produceRadioButton(Composite parent) {

        Button radioButton = new Button(parent, SWT.RADIO);

        return radioButton;
    }

    private Label produceSeparator(Composite parent, int span) {

        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalSpan = span;
        separator.setLayoutData(gridData);

        return separator;
    }

    /**
     * Thread-safe method that disposes the instance of this Singleton class.
     * <p>
     * This method is intended to be call from
     * {@link org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)}
     * to free the reference to itself.
     */
    public static void dispose() {
        if (instance != null) {
            instance = null;
        }
    }
}
