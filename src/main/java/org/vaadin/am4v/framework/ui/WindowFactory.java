package org.vaadin.am4v.framework.ui;

import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.WindowApplicationModel;

import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * <b>Experimental</b> base class for UIs that observe {@link WindowApplicationModel}s and actually show and hide the
 * window.
 */
public abstract class WindowFactory<M extends WindowApplicationModel> implements ApplicationModel.Observer<M> {

    private M applicationModel;
    private Window visibleWindow;

    /**
     * Creates a new window factory without a model. The model must be explicitly assigned using
     * {@link #setApplicationModel(WindowApplicationModel)}.
     */
    public WindowFactory() {
    }

    /**
     * Creates a new window factory with the specified model.
     * 
     * @param applicationModel the application model to use.
     */
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

    /**
     * Returns the application model currently being observed.
     * 
     * @return the application model, may be {@code null}.
     */
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

    /**
     * Creates the window that will be shown. The window factory will take care of actually showing the window
     * when it is time.
     * 
     * @return a new window to show.
     */
    protected abstract Window createWindow();
}
