package com.visitscotland.brxm.model;

import java.util.ArrayList;
import java.util.List;

public class IssueList {
    private List<String> errorMessages = new ArrayList<>();

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void addError(String message){
        errorMessages.add(message);
    }
}
