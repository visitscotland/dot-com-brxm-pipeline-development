package com.visitscotland.brxm.translation;

import org.hippoecm.editor.type.JcrTypeLocator;
import org.hippoecm.frontend.types.IFieldDescriptor;
import org.hippoecm.frontend.types.ITypeDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MockITypeDescriptorBuilder {
    Boolean isNode;
    String type;
    JcrTypeLocator mockTypeLocator;
    Map<String, IFieldDescriptor> declaredFieldMap;
    List<String> superTypeList = new ArrayList<>();

    public MockITypeDescriptorBuilder withDeclaredFieldMap(Map<String, IFieldDescriptor> declaredFieldMap) {
        this.declaredFieldMap = declaredFieldMap;
        return this;
    }

    public MockITypeDescriptorBuilder addToJcrTypeLocator(JcrTypeLocator mockTypeLocator) {
        this.mockTypeLocator = mockTypeLocator;
        return this;
    }

    public MockITypeDescriptorBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public MockITypeDescriptorBuilder isNode(boolean isNode) {
        this.isNode = isNode;
        return this;
    }

    public MockITypeDescriptorBuilder withSuperType(String type) {
        superTypeList.add(type);
        return this;
    }

    public MockITypeDescriptorBuilder withSuperType(ITypeDescriptor superType) {
        superTypeList.add(superType.getType());
        return this;
    }

    public ITypeDescriptor build() throws Exception {
        ITypeDescriptor mockTypeDescriptor = mock(ITypeDescriptor.class);

        if ( null != declaredFieldMap ) {
            when(mockTypeDescriptor.getDeclaredFields()).thenReturn(declaredFieldMap);
        }

        if ( null != type ) {
            lenient().when(mockTypeDescriptor.getType()).thenReturn(type);
            if ( null != mockTypeLocator ) {
                when(mockTypeLocator.locate(eq(type))).thenReturn(mockTypeDescriptor);
            }
        }

        if ( null != isNode ) {
            when(mockTypeDescriptor.isNode()).thenReturn(isNode);
        }

        lenient().when(mockTypeDescriptor.getSuperTypes()).thenReturn(superTypeList);

        return mockTypeDescriptor;
    }
}
