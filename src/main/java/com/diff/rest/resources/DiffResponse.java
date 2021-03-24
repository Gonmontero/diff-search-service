package com.diff.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DiffResponse {

    private String id;
    private byte[] left;
    private byte[] right;

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
