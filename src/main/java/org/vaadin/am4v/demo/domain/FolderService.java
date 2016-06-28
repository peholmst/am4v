package org.vaadin.am4v.demo.domain;

import java.util.*;

public class FolderService {

    private static final FolderService INSTANCE = new FolderService();

    private final Map<UUID, Folder> uuidToFolderMap = new HashMap<>();
    private final Map<UUID, List<Folder>> parentToChildrenMap = new HashMap<>();
    private final Folder root;

    final Folder inbox;
    final Folder junk;
    final Folder trash;

    public static FolderService getInstance() {
        return INSTANCE;
    }

    private FolderService() {
        root = new Folder(null, "Mail", false);
        uuidToFolderMap.put(root.getUuid(), root);

        inbox = addFolder(root, "Inbox", false);
        addFolder(root, "Drafts", false);
        addFolder(root, "Sent", false);
        junk = addFolder(root, "Junk", false);
        trash = addFolder(root, "Trash", false);
    }

    public Folder getRoot() {
        return root;
    }

    public synchronized Optional<Folder> getFolder(UUID uuid) {
        return Optional.ofNullable(uuidToFolderMap.get(uuid));
    }

    public Optional<Folder> getParent(Folder child) {
        if (child.getParentUuid() != null) {
            return getFolder(child.getParentUuid());
        } else {
            return Optional.empty();
        }
    }

    public List<Folder> getChildren(Folder parent) {
        return getChildren(parent.getUuid());
    }

    public synchronized List<Folder> getChildren(UUID parentUuid) {
        return new ArrayList<>(parentToChildrenMap.getOrDefault(parentUuid, Collections.emptyList()));
    }

    public synchronized Folder addFolder(Folder parent, String name, boolean userCreated) {
        Folder folder = new Folder(Objects.requireNonNull(parent).getUuid(), name, userCreated);
        uuidToFolderMap.put(folder.getUuid(), folder);
        List<Folder> siblings = parentToChildrenMap.get(folder.getParentUuid());
        if (siblings == null) {
            siblings = new ArrayList<>();
            parentToChildrenMap.put(folder.getParentUuid(), siblings);
        }
        siblings.add(folder);
        return folder;
    }
}
