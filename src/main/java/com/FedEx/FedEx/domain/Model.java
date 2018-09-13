package com.FedEx.FedEx.domain;

public class Model {
    private String command;

    public Model(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
