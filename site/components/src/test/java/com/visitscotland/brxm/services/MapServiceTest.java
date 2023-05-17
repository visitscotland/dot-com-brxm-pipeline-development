package com.visitscotland.brxm.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.CategoryInfo;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyException;
import org.onehippo.taxonomy.impl.TaxonomyImpl;

import javax.jcr.RepositoryException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MapServiceTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private HippoUtilsService hippoUtilsService;

    @Mock
    ContentLogger logger;

    @InjectMocks
    MapService service;

    @Test
    @DisplayName("VS-3996 - Create category node for maps json data")
    void buildCategoryNode() {
        ObjectNode expected = mockCategory("acco", "Accommodation");

        when(mapper.createObjectNode()).thenReturn(expected);

        ObjectNode categoryNode = service.buildCategoryNode("acco", "Accommodation");

        assertNotNull(categoryNode);
        assertEquals(expected, categoryNode);
        assertTrue(categoryNode.has(MapService.ID));
        assertTrue(categoryNode.has(MapService.LABEL));
    }

    @Test
    @DisplayName("VS-3996 - add filters node for maps json data with childs/subcategory")
    void addFilterNodeWithSubcategory() throws TaxonomyException, RepositoryException {
        ObjectNode expected = mockCategory("acco", "Accommodation");

        Category category = mock(Category.class);
        CategoryInfo categoryInfo = mock(CategoryInfo.class);
        when(category.getKey()).thenReturn("acco");
        when(category.getInfo(Locale.ENGLISH)).thenReturn(categoryInfo);
        when(categoryInfo.getName()).thenReturn("Accommodation");

        Category subCategory = mock(Category.class);
        CategoryInfo subCategoryInfo = mock(CategoryInfo.class);
        when(subCategory.getKey()).thenReturn("hotel");
        when(subCategory.getInfo(Locale.ENGLISH)).thenReturn(subCategoryInfo);
        when(subCategoryInfo.getName()).thenReturn("Hotel");

        List<Category> categories = new ArrayList<Category> ();
        categories.add(subCategory);
        Answer<List<? extends Category>> answer = invocationOnMock -> categories;
        when(category.getChildren()).thenAnswer(answer);

        ArrayNode childrenArray = Mockito.mock(ArrayNode.class);
        when(mapper.createObjectNode()).thenReturn(expected);
        when(mapper.createArrayNode()).thenReturn(childrenArray);


        ObjectNode categoryNode = service.addFilterNode(category, Locale.ENGLISH);

        assertNotNull(categoryNode);

        assertTrue(categoryNode.has(MapService.SUBCATEGORY));
    }

    @Test
    @DisplayName("VS-3996 - add filters node for maps json data with no childs/subcategory")
    void addFilterNodeNoSubcategory() {
        ObjectNode expected = mockCategory("acco", "Accommodation");

        Category category = mock(Category.class);
        CategoryInfo categoryInfo = mock(CategoryInfo.class);
        when(category.getKey()).thenReturn("acco");
        when(category.getInfo(Locale.ENGLISH)).thenReturn(categoryInfo);
        when(categoryInfo.getName()).thenReturn("Accommodation");
        when(category.getChildren()).thenReturn(new ArrayList<>());

        when(mapper.createObjectNode()).thenReturn(expected);

        ObjectNode categoryNode = service.addFilterNode(category, Locale.ENGLISH);

        assertNotNull(categoryNode);
        assertEquals(expected, categoryNode);
        assertFalse(categoryNode.has(MapService.SUBCATEGORY));
    }

    private ObjectNode mockCategory(String id, String label){
        ObjectNode expected = (new ObjectMapper()).createObjectNode();
        expected.put(MapService.ID,id);
        expected.put(MapService.LABEL,label);

        return expected;
    }

}
