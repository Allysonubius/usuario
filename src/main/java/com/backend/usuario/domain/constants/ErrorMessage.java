package com.backend.usuario.domain.constants;

public enum ErrorMessage {
    WRONGDATA("Input data is missing or is incorrect");
    private final String value;
    private ErrorMessage(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
