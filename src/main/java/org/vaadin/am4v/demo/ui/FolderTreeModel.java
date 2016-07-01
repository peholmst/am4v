package org.vaadin.am4v.demo.ui;

import org.vaadin.am4v.demo.domain.Folder;
import org.vaadin.am4v.demo.domain.FolderService;
import org.vaadin.am4v.framework.model.*;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Notification;

/**
 * This is the model containing all the folders. It is a direct child of the {@code MainModel}.
 */
public class FolderTreeModel extends ApplicationModel {

    private boolean initialized = false;

    /**
     * Hierarchical container of the folder tree. Exposing this as a Vaadin container is a bit problematic if there
     * are more than one view observing the model since certain operations such as filtering and sorting are done
     * on the container level. We would probably need a new set of observable collections that in turn can be
     * observed by containers.
     */
    public final Container.Hierarchical tree = new HierarchicalContainer() {
        {
            addContainerProperty("name", String.class, "");
        }
    };

    /**
     * Property containing the currently selected folder.
     */
    public final ApplicationProperty<Folder> selected = new ApplicationProperty<>(null, Folder.class);

    /**
     * Action that refreshes the entire folder tree and clears the selection.
     */
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

    /**
     * Contextual action that removes the passed in folder. If no folder is passed in, the current selection is used
     * instead.
     */
    public final ContextualApplicationAction<Folder> removeFolder = new ContextualApplicationAction<Folder>() {
        @Override
        public void run(Folder context) {
            if (context == null) {
                context = selected.getValue();
            }
            getNotificationStrategy()
                .showNotification("This would remove " + context.getName() + " but is not implemented yet");
        }

        @Override
        public boolean isEnabled(Folder context) {
            return context != null && context.isUserCreated();
        }
    };

    /**
     * Contextual action that adds a new folder to the passed in folder. If no folder is passed in, the current
     * selection is used instead.
     */
    public final ContextualApplicationAction<Folder> addFolder = new ContextualApplicationAction<>((action, parent) -> {
        if (parent == null) {
            parent = selected.getValue();
        }
        getWindowStrategy().showWindow(AddFolderWindow.WINDOW_NAME,
            new Parameters().setParameter(Folder.class, parent).setParameter(ApplicationModel.class, this));
    });

    public FolderTreeModel(MainModel parent) {
        super(parent);
        // When a new folder is added, we want to refresh the tree.
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
