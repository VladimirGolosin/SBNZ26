package com.ftn.sbnz.dto;

public class CultureAnswerDTO {

    private String instructions;
    private boolean isRightTime;


    public CultureAnswerDTO(String instructions, boolean isRightTime) {
        this.instructions = instructions;
        this.isRightTime = isRightTime;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isRightTime() {
        return isRightTime;
    }

    public void setRightTime(boolean rightTime) {
        isRightTime = rightTime;
    }
}
