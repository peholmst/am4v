package org.vaadin.am4v.framework.ui;

import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.WindowApplicationModel;

public abstract class WindowFactory<M extends WindowApplicationModel> implements ApplicationModel.Observer<M> {

    private M applicationModel;
    private Window visibleWindow;

    public WindowFactory() {
    }

    public WindowFactory(M applicationModel) {
        setApplicationModel(applicationModel);
    }

    @Override
    public final void setApplicationModel(M applicationModel) {
        if (this.applicationModel != null) {
            this.applicationModel.windowVisible().removeValueChangeListener(this::onWindowVisibilityChanged);
        }
        this.applicationModel = applicationModel;
        if (this.applicationModel != null) {
            this.applicationModel.windowVisible().addValueChangeListener(this::onWindowVisibilityChanged);
        }
    }

    public final M getApplicationModel() {
        return applicationModel;
    }

    private void onWindowVisibilityChanged(Property.ValueChangeEvent event) {
        if (applicationModel.windowVisible().getValue()) {
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
