package org.vaadin.am4v.demo.ui;

import javax.servlet.annotation.WebServlet;

import org.vaadin.am4v.framework.ui.ProviderBasedWindowStrategy;
import org.vaadin.am4v.framework.ui.SingleWindowProvider;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * The main UI of the demo application. The entire UI could have been built without any models at all. This means that
 * Vaadin Designer could have been used, or that the UI could have been built first and the models added afterwards as
 * needed.
 */
@Theme(value = ValoTheme.THEME_NAME)
public class MainUI extends UI {

    private final MainModel mainModel = new MainModel();
    private final FolderTreeModel folderTreeModel = new FolderTreeModel(mainModel);
    private final MessageListModel messageListModel = new MessageListModel(folderTreeModel);
    private final MessageModel messageModel = new MessageModel(messageListModel);

    @Override
    protected void init(VaadinRequest request) {
        // The idea is to set up the WindowStrategy in the same way you would set up the Navigator if you were using
        // views and the navigation API.
        ProviderBasedWindowStrategy windowStrategy = new ProviderBasedWindowStrategy();
        windowStrategy.addWindow(AddFolderWindow.class);
        mainModel.setWindowStrategy(windowStrategy);

        final HorizontalSplitPanel rootPanel = new HorizontalSplitPanel();
        rootPanel.setSizeFull();
        rootPanel.setSplitPosition(20, Unit.PERCENTAGE);
        setContent(rootPanel);

        final FolderTreeView folderTreeView = new FolderTreeView();
        folderTreeView.setApplicationModel(folderTreeModel);
        folderTreeView.setSizeFull();
        rootPanel.setFirstComponent(folderTreeView);

        final VerticalSplitPanel messagePanel = new VerticalSplitPanel();
        messagePanel.setSizeFull();
        rootPanel.setSecondComponent(messagePanel);

        final MessageListView messageListView = new MessageListView();
        messageListView.setApplicationModel(messageListModel);
        messageListView.setSizeFull();
        messagePanel.setFirstComponent(messageListView);

        final MessageView messageView = new MessageView();
        messageView.setApplicationModel(messageModel);
        messageView.setSizeFull();

        final VerticalLayout noMessageSelectedView = new VerticalLayout(new Label("No message selected"));
        noMessageSelectedView.setMargin(true);
        messagePanel.setSecondComponent(noMessageSelectedView);

        // The main view is responsible for showing or hiding the views. This makes the view implementations
        // simpler.
        messageListModel.selected.addValueChangeListener(evt -> {
            if (evt.getProperty().getValue() == null) {
                messagePanel.setSecondComponent(noMessageSelectedView);
            } else {
                messagePanel.setSecondComponent(messageView);
            }
        });
    }

    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    @WebServlet(urlPatterns = "/*")
    public static class Servlet extends VaadinServlet {
    }
}
