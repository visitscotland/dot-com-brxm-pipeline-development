package com.visitscotland.brxm.translation;

import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import org.hippoecm.editor.template.JcrTemplateStore;
import org.hippoecm.editor.type.JcrTypeLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.jcr.Session;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
public class TranslationConfig {

    @Bean
    public SessionFactory jcrSessionFactory() {
        return new SessionFactory();
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public Session jcrSession(SessionFactory jcrSessionFactory) {
        return jcrSessionFactory.getJcrSession();
    }

    @Bean
    public JcrDocumentFactory jcrDocumentFactory() {
        return new JcrDocumentFactory();
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public JcrTypeLocator jcrTypeLocator() {
        return new JcrTypeLocator();
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public JcrTemplateStore jcrTemplateStore(JcrTypeLocator locator) {
        return new JcrTemplateStore(locator);
    }

}
