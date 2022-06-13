package com.visitscotland.brxm.components.navigation.info;

import org.hippoecm.hst.core.parameters.Parameter;

public interface GeneralPageComponentInfo  {
    @Parameter(
            name = "status",
            defaultValue = "200",
            required = true
    )
    String getStatus();
}

