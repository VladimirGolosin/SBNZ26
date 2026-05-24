package com.ftn.sbnz.dto;

public class CultureQuestionRequestDTO {

    String culture;


    public CultureQuestionRequestDTO(String culture) {
        this.culture = culture;

    }

	public String getCulture() {
        return culture;
    }

    public CultureQuestionRequestDTO setCulture(String culture) {
        this.culture = culture;
        return this;
    }

}
