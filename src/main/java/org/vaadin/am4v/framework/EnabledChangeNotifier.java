package org.vaadin.am4v.framework;

import java.io.Serializable;

public interface EnabledChangeNotifier extends Serializable {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    void addEnabledChangeListener(EnabledChangeListener listener);

    void removeEnabledChangeListener(EnabledChangeListener listener);
}
