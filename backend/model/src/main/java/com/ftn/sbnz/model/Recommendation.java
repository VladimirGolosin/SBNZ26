package com.ftn.sbnz.model;

public class Recommendation {

    public enum Type {
        ACTION_DUE,
        NEEDS_IRRIGATION,
        HARVEST_READY,
        PROBLEM_SOLUTION
    }

    private Type type;
    private String message;

    public Recommendation() {
    }

    public Recommendation(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}