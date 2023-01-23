package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.DevModule;
import com.visitscotland.brxm.hippobeans.SimpleDevModule;
import org.springframework.stereotype.Controller;

@Controller
public class DevModuleFactory {

    public SimpleDevModule getModule(DevModule document){
        return new SimpleDevModule(document);
    }
}
