package org.vaadin.am4v.framework;

import java.io.Serializable;

public interface VisibleChangeNotifier extends Serializable {

    boolean isVisible();

    void setVisible(boolean visible);

    void addVisibleChangeListener(VisibleChangeListener listener);

    void removeVisibleChangeListener(VisibleChangeListener listener);
}
