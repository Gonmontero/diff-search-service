package com.diff.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Diff {

    @Id
    private String id;
    private byte[] left;
    private byte[] right;

    public Diff() {

    }
    public Diff(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getLeft() {
        return left;
    }

    public void setLeft(byte[] left) {
        this.left = left;
    }

    public byte[] getRight() {
        return right;
    }

    public void setRight(byte[] right) {
        this.right = right;
    }

}
