package com.disaster.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Base abstract class demonstrating ENCAPSULATION and INHERITANCE.
 * Encapsulates common identification and timestamp auditing attributes.
 */
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public BaseEntity() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public BaseEntity(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isPersisted() {
        return this.id != null && this.id > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

