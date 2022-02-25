package com.visitscotland.brxm.cms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.context.annotation.*;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration()
@Profile("dbActuator")
@PropertySource(value = "classpath:actuator.properties")
public class SBActuatorConfiguration {

    @Value("${app.datasource.name}")
    String datasourceName;

    @Bean(destroyMethod = "")
    DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
        final JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("java:comp/env/jdbc/" + datasourceName);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }

    @Bean
    HealthIndicator dbHealthIndicator(final DataSource dataSource) {
        return new DataSourceHealthIndicator(dataSource, "SELECT 1");
    }
}