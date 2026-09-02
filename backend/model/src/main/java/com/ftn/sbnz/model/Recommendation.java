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
    private ActionName actionName;
    private SolutionName solutionName;
    private ProblemName problemName;

    public Recommendation() {
    }

    public Recommendation(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Recommendation(Type type, String message, ActionName actionName) {
        this.type = type;
        this.message = message;
        this.actionName = actionName;
    }

    public Recommendation(Type type, String message, SolutionName solutionName) {
        this.type = type;
        this.message = message;
        this.solutionName = solutionName;
    }

    public Recommendation(Type type, String message, SolutionName solutionName, ProblemName problemName) {
        this.type = type;
        this.message = message;
        this.solutionName = solutionName;
        this.problemName = problemName;
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

    public ActionName getActionName() {
        return actionName;
    }

    public void setActionName(ActionName actionName) {
        this.actionName = actionName;
    }

    public SolutionName getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(SolutionName solutionName) {
        this.solutionName = solutionName;
    }

    public ProblemName getProblemName() {
        return problemName;
    }

    public void setProblemName(ProblemName problemName) {
        this.problemName = problemName;
    }
}