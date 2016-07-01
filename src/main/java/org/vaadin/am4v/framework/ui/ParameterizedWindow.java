package org.vaadin.am4v.framework.ui;

import java.io.Serializable;

import org.vaadin.am4v.framework.model.Parameters;

/**
 * Interface to be implemented by {@link com.vaadin.ui.Window} subclasses that want to be instantiated automatically
 * by the {@link SingleWindowProvider} without the need for a
 * {@link org.vaadin.am4v.framework.ui.SingleWindowProvider.WindowFactory}.
 * 
 * @see WindowName
 */
public interface ParameterizedWindow extends Serializable {

    /**
     * Sets the parameters passed to the {@link WindowProvider#getWindow(String, Parameters)} method. This method
     * is called after the window has been created but before it has been shown.
     * 
     * @param parameters the parameters (never {@code null}).
     */
    void setParameters(Parameters parameters);
}
