package org.vaadin.am4v.framework;

import java.io.Serializable;

@FunctionalInterface
public interface VisibleChangeListener extends Serializable {

    void onVisibleChanged(VisibleChangeNotifier source);
}
