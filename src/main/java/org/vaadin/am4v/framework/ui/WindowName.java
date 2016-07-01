package org.vaadin.am4v.framework.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used by the {@link SingleWindowProvider} to determine the name of a {@link ParameterizedWindow} when
 * one has not been explicitly specified.
 * 
 * @see SingleWindowProvider#SingleWindowProvider(Class)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WindowName {

    /**
     * The name of the window.
     */
    String value();
}
