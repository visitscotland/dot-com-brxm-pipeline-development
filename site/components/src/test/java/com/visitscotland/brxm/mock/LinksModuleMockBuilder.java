package com.visitscotland.brxm.mock;

import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import com.visitscotland.brxm.model.megalinks.LinksModule;
import com.visitscotland.brxm.model.megalinks.MultiImageLinksModule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class LinksModuleMockBuilder {

    private final LinksModule module;
    private final List<EnhancedLink> links;

    public LinksModuleMockBuilder() {
        links = new ArrayList<>();
        module = mock(MultiImageLinksModule.class);
        lenient().when(module.getType()).thenReturn(MultiImageLinksModule.class.getSimpleName());
    }

    public LinksModule build() {
        when(module.getLinks()).thenReturn(links);
        return module;
    }

    public <T extends LinksModule<?>> LinksModuleMockBuilder type(Class<T> type) {
        when(module.getType()).thenReturn(type.getSimpleName());
        return this;
    }

    public LinksModuleMockBuilder title(String title) {
        when(module.getTitle()).thenReturn(title);
        return this;
    }

    public LinksModuleMockBuilder withLink(EnhancedLink link) {
        links.add(link);
        return this;
    }
}
