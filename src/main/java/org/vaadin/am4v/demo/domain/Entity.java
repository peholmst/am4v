package org.vaadin.am4v.demo.domain;

import java.util.UUID;

public abstract class Entity {

    private final UUID uuid;

    public Entity(UUID uuid) {
        this.uuid = uuid;
    }

    public Entity() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Entity entity = (Entity) o;

        return uuid.equals(entity.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
