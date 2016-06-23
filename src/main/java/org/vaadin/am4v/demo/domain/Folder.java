package org.vaadin.am4v.demo.domain;

import java.util.UUID;

public class Folder extends Entity {

    private UUID parentUuid;
    private String name;

    public UUID getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(UUID parentUuid) {
        this.parentUuid = parentUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
