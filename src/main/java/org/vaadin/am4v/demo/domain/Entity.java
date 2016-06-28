package org.vaadin.am4v.demo.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class Entity implements Serializable, Cloneable {

    private final UUID uuid;

    public Entity(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid);
    }

    public Entity() {
        this.uuid = UUID.randomUUID();
    }

    public Entity(Entity original) {
        this.uuid = Objects.requireNonNull(original).getUuid();
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

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError("Could not clone entity", ex);
        }
    }
}
