package org.bloomreach.xm.cms;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * This class includes our customized packages as part of BloomReach Context
 */
@Configuration
@ComponentScan(basePackages = "com.visitscotland.brxm.cms.config")
@PropertySource(value = "classpath:actuator.properties")
public class BrxmActuatorConfiguration {
}
