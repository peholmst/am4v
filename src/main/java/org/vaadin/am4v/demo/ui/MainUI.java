package org.vaadin.am4v.demo.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(value = ValoTheme.THEME_NAME)
public class MainUI extends UI {

    private final MainModel mainModel = new MainModel();
    private final AddFolderModel addFolderModel = new AddFolderModel(mainModel);
    private final FolderTreeModel folderTreeModel = new FolderTreeModel(mainModel, addFolderModel);
    private final MessageListModel messageListModel = new MessageListModel(folderTreeModel);
    private final MessageModel messageModel = new MessageModel(messageListModel);

    @Override
    protected void init(VaadinRequest request) {
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

        new AddFolderView(addFolderModel);

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
