package org.vaadin.am4v.framework;

import java.io.Serializable;

@FunctionalInterface
public interface EnabledChangeListener extends Serializable {

    void onEnabledChange(EnabledChangeNotifier source);
}
