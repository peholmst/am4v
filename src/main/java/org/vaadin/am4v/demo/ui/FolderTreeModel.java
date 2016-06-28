package org.vaadin.am4v.demo.ui;

import com.vaadin.ui.Notification;
import org.vaadin.am4v.demo.domain.Folder;
import org.vaadin.am4v.demo.domain.FolderService;
import org.vaadin.am4v.framework.model.ApplicationAction;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.ApplicationProperty;
import org.vaadin.am4v.framework.model.ContextualApplicationAction;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

public class FolderTreeModel extends ApplicationModel {

    private boolean initialized = false;
    private AddFolderModel addFolderModel;

    public final Container.Hierarchical tree = new HierarchicalContainer() {
        {
            addContainerProperty("name", String.class, "");
        }
    };

    public final ApplicationProperty<Folder> selected = new ApplicationProperty<>(null, Folder.class);

    public final ApplicationAction refresh = new ApplicationAction(action -> {
        selected.setValue(null);
        tree.removeAllItems();
        Folder root = FolderService.getInstance().getRoot();
        addFolder(root, null);
        addChildren(root);
        if (initialized) {
            getNotificationStrategy().showNotification("Folders refreshed", Notification.Type.TRAY_NOTIFICATION);
        }
    });

    private void addChildren(Folder parent) {
        FolderService.getInstance().getChildren(parent).forEach(c -> {
            addFolder(c, parent);
            addChildren(c);
        });
    }

    private void addFolder(Folder folder, Folder parent) {
        Item item = tree.addItem(folder);
        item.getItemProperty("name").setValue(folder.getName());
        tree.setChildrenAllowed(folder, false);
        if (parent != null) {
            tree.setChildrenAllowed(parent, true);
            tree.setParent(folder, parent);
        }
    }

    public final ContextualApplicationAction<Folder> removeFolder = new ContextualApplicationAction<Folder>() {
        @Override
        public void run(Folder context) {
            if (context == null) {
                context = selected.getValue();
            }
            getNotificationStrategy().showNotification("This would remove " + context.getName() + " but is not implemented yet");
        }

        @Override
        public boolean isEnabled(Folder context) {
            return context != null && context.isUserCreated();
        }
    };

    public final ContextualApplicationAction<Folder> addFolder = new ContextualApplicationAction<>((action, parent) -> {
        if (parent == null) {
            parent = selected.getValue();
        }
        if (addFolderModel != null) {
            addFolderModel.show(parent);
        }
    });

    public FolderTreeModel(MainModel parent, AddFolderModel addFolderModel) {
        super(parent);
        this.addFolderModel = addFolderModel;
        registerMessageHandler(FolderAdded.class, (source, msg) -> refresh.run());
        selected.addValueChangeListener(evt -> {
            addFolder.setEnabled(selected.getValue() != null);
            removeFolder.setEnabled(selected.getValue() != null && selected.getValue().isUserCreated());
        });
        selected.setValue(null);
        refresh.run();
        initialized = true;
    }
}
