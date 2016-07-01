package org.vaadin.am4v.framework.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Basically a wrapper around a {@link Map} with some helper methods to make it easier to access and set parameters.
 */
public class Parameters implements Serializable {

    private final Map<Object, Object> backingMap;

    /**
     * Creates a new {@code Parameters} object with an empty backing {@link HashMap}.
     */
    public Parameters() {
        this.backingMap = new HashMap<>();
    }

    /**
     * Creates a new {@code Parameters} object with the given backing {@link Map}.
     * 
     * @param backingMap
     */
    public Parameters(Map<Object, Object> backingMap) {
        this.backingMap = Objects.requireNonNull(backingMap);
    }

    /**
     * Returns the backing {@link Map}. Any changes made to this map are immediately reflected in the {@link Parameters}
     * object.
     * 
     * @return a map (never {@code null}).
     */
    public Map<Object, Object> getBackingMap() {
        return backingMap;
    }

    /**
     * Gets the parameter with the given name and type.
     * 
     * @param name the name of the parameter.
     * @param expectedType the expected type of the parameter.
     * @return the parameter value.
     * @throws IllegalArgumentException if no such parameter exists.
     * @throws ClassCastException if the parameter exists but cannot be cast to the desired type.
     */
    public <T> T getParameter(Object name, Class<T> expectedType) {
        if (backingMap.containsKey(name)) {
            return expectedType.cast(backingMap.get(name));
        } else {
            throw new IllegalArgumentException("No such parameter found: " + name);
        }
    }

    /**
     * Gets the parameter with the given name and type.
     *
     * @param name the name of the parameter.
     * @param expectedType the expected type of the parameter.
     * @param defaultValue the default value to return if the parameter does not exist.
     * @return the parameter value or {@code defaultValue} if not found.
     * @throws ClassCastException if the parameter exists but cannot be cast to the desired type.
     */
    public <T> T getParameter(Object name, Class<T> expectedType, T defaultValue) {
        return expectedType.cast(backingMap.getOrDefault(name, defaultValue));
    }

    /**
     * Gets the parameter with the given type, using the type itself as the parameter's name.
     * 
     * @param expectedType the type (and name) of the parameter.
     * @return the parameter value.
     * @throws IllegalArgumentException if no such parameter exists.
     * @throws ClassCastException if the parameter exists but cannot be cast to the desired type.
     */
    public <T> T getParameter(Class<T> expectedType) {
        return getParameter(expectedType, expectedType);
    }

    /**
     * Gets the parameter with the given type, using the type itself as the parameter's name.
     *
     * @param expectedType the type (and name) of the parameter.
     * @param defaultValue the default value to return if the parameter does not exist.
     * @return the parameter value or {@code defaultValue} if not found.
     * @throws ClassCastException if the parameter exists but cannot be cast to the desired type.
     */
    public <T> T getParameter(Class<T> expectedType, T defaultValue) {
        return getParameter(expectedType, expectedType, defaultValue);
    }

    /**
     * Sets the parameter with the given name to the given value.
     * 
     * @param name the name of the parameter.
     * @param value the value of the parameter.
     * @return {@code this} object, to allow for method chaining.
     */
    public Parameters setParameter(Object name, Object value) {
        backingMap.put(name, value);
        return this;
    }

    /**
     * Sets the parameter with the given type to the given value, using the type as the parameter's name.
     * 
     * @param parameterType the type of the parameter.
     * @param value the name of the parameter.
     * @return {@code this} object, to allow for method chaining.
     */
    public <T> Parameters setParameter(Class<T> parameterType, T value) {
        backingMap.put(parameterType, value);
        return this;
    }
}
