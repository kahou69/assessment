package com.example.assessment.etiqa.model;

public enum EmailType {
    OFFICE(true),
    PERSONAL(true);

    private final boolean required;

    EmailType(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

}
