package com.visitscotland.brxm.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "profiles")
public class ProfileActuator {

    @Autowired
    private Environment environment;

    @ReadOperation
    public String[] activeProfile() {
        return environment.getActiveProfiles();
    }
}
