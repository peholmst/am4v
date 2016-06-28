package org.vaadin.am4v.demo.domain;

import java.util.Objects;
import java.util.UUID;

public class Folder extends Entity {

    private final UUID parentUuid;
    private final String name;
    private final boolean userCreated;

    public Folder(UUID uuid, UUID parentUuid, String name, boolean userCreated) {
        super(uuid);
        this.parentUuid = parentUuid;
        this.name = Objects.requireNonNull(name);
        this.userCreated = userCreated;
    }

    public Folder(UUID parentUuid, String name, boolean userCreated) {
        this.parentUuid = parentUuid;
        this.name = Objects.requireNonNull(name);
        this.userCreated = userCreated;
    }

    public Folder(Folder folder) {
        super(folder);
        this.parentUuid = folder.getParentUuid();
        this.name = folder.getName();
        this.userCreated = folder.userCreated;
    }

    public UUID getParentUuid() {
        return parentUuid;
    }

    public String getName() {
        return name;
    }

    public boolean isUserCreated() {
        return userCreated;
    }

    @Override
    public String toString() {
        return String.format("Folder[uuid=%s, name=%s]", getUuid(), name);
    }
}
