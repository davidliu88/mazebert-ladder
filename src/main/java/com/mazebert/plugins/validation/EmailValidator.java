package com.mazebert.plugins.validation;

public class EmailValidator {
    public boolean isValid(String email) {
        return email != null && email.contains("@");
    }
}
