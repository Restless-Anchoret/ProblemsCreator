package com.ran.interaction.components;

public class SelectItem {

    private String value;
    private String presentation;

    public SelectItem(String value, String presentation) {
        this.value = value;
        this.presentation = presentation;
    }

    public String getValue() {
        return value;
    }

    public String getPresentation() {
        return presentation;
    }
    
}