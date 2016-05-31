package org.vaadin.am4v.framework;

import java.io.Serializable;

import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public abstract class WindowFactory<M extends WindowApplicationModel> implements Serializable {

    private M windowApplicationModel;
    private Window visibleWindow;

    public WindowFactory() {
    }

    public WindowFactory(M windowApplicationModel) {
        this.windowApplicationModel = windowApplicationModel;
    }

    public final void setWindowApplicationModel(M windowApplicationModel) {
        if (this.windowApplicationModel != null) {
            this.windowApplicationModel.windowVisible().removeValueChangeListener(this::onWindowVisibilityChanged);
        }
        this.windowApplicationModel = windowApplicationModel;
        if (this.windowApplicationModel != null) {
            this.windowApplicationModel.windowVisible().addValueChangeListener(this::onWindowVisibilityChanged);
        }
    }

    public final M getWindowApplicationModel() {
        return windowApplicationModel;
    }

    private void onWindowVisibilityChanged(Property.ValueChangeEvent event) {
        if (windowApplicationModel.windowVisible().getValue()) {
            showWindow();
        } else {
            hideWindow();
        }
    }

    private void showWindow() {
        if (visibleWindow == null) {
            visibleWindow = createWindow();
            visibleWindow.addCloseListener(this::onWindowClosed);
            UI.getCurrent().addWindow(visibleWindow);
        }
    }

    private void hideWindow() {
        if (visibleWindow != null) {
            visibleWindow.removeCloseListener(this::onWindowClosed);
            visibleWindow.close();
            visibleWindow = null;
        }
    }

    private void onWindowClosed(Window.CloseEvent event) {
        hideWindow();
    }

    protected abstract Window createWindow();
}
