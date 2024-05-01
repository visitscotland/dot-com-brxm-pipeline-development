package com.visitscotland.brxm.model;

import java.util.List;

public class PersonalisationModule extends Module {

    private List<Module> modules;

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
