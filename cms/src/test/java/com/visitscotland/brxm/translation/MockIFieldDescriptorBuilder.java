package com.visitscotland.brxm.translation;

import org.hippoecm.frontend.types.IFieldDescriptor;
import org.hippoecm.frontend.types.ITypeDescriptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MockIFieldDescriptorBuilder {
    String name;
    String path;
    Boolean isMultiple;
    Boolean isPrimary;
    ITypeDescriptor typeDescriptor;

    public MockIFieldDescriptorBuilder withTypeDescriptor(ITypeDescriptor typeDescriptor) {
        this.typeDescriptor = typeDescriptor;
        return this;
    }

    public MockIFieldDescriptorBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MockIFieldDescriptorBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public MockIFieldDescriptorBuilder isMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
        return this;
    }

    public MockIFieldDescriptorBuilder isPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
        return this;
    }

    public IFieldDescriptor build() {
        IFieldDescriptor mockField = mock(IFieldDescriptor.class);
        if (null != name) {
            when(mockField.getName()).thenReturn(name);
        }
        if (null != path) {
            when(mockField.getPath()).thenReturn(path);
        }
        if (null != isMultiple) {
            when(mockField.isMultiple()).thenReturn(isMultiple.booleanValue());
        }
        if (null != isPrimary) {
            when(mockField.isPrimary()).thenReturn(isPrimary.booleanValue());
        }
        if (null != typeDescriptor) {
            when(mockField.getTypeDescriptor()).thenReturn(typeDescriptor);
        }
        return mockField;
    }
}
