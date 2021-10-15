package com.photomemories.domain.service;

import java.io.Serializable;
import java.util.Objects;

public class PhotoMemoriesResponse<T> implements Serializable {
    private static final long serialVersionUID = 5671364603837343946L;
    private boolean confirmation;
    private transient T cargo;

    public PhotoMemoriesResponse() {
    }

    public PhotoMemoriesResponse(boolean confirmation, T cargo) {
        this.confirmation = confirmation;
        this.cargo = cargo;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public T getCargo() {
        return cargo;
    }

    public void setCargo(T cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoMemoriesResponse<?> that = (PhotoMemoriesResponse<?>) o;
        return confirmation == that.confirmation && Objects.equals(cargo, that.cargo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmation, cargo);
    }

    @Override
    public String toString() {
        return "PhotoMemoriesResponse{" +
                "confirmation=" + confirmation +
                ", cargo=" + cargo +
                '}';
    }
}
