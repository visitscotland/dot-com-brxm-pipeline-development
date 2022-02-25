package com.visitscotland.brxm.translation.plugin.menu;

import com.visitscotland.brxm.translation.plugin.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.tester.WicketTester;
import org.hippoecm.frontend.session.UserSession;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.repository.api.HippoSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TranslationActionTest {
    @Mock
    LoadableDetachableModel<String> mockNameModel;
    private WicketTester wicket;
    @Mock
    private TranslationWorkflowPlugin mockWorkflowPlugin;
    @Mock
    private IModel<ILocaleProvider.HippoLocale> mockLocaleModel;
    @Mock
    private TranslationWorkflow mockWorkflow;
    @Mock
    private HippoSession mockHippoSession;
    private List<ILocaleProvider.HippoLocale> availableLocales;
    @Mock
    private DocumentTranslator mockTranslator;
    private List<ChangeSet> changeSetList;
    @Mock
    private UserSessionFactory mockUserSessionFactory;
    @Mock
    private UserSession mockUserSession;
    private TranslationAction action;

    @BeforeEach
    public void beforeEach() throws Exception {
        // This is needed to initialise the Wicket application so we can create the component.
        wicket = new WicketTester();

        availableLocales = new ArrayList<>();
        changeSetList = new ArrayList<>();
        when(mockTranslator.buildChangeSetList(any(), any())).thenReturn(changeSetList);
        when(mockWorkflowPlugin.getAvailableLocales()).thenReturn(availableLocales);
        action = new TranslationAction(
                mockWorkflowPlugin,
                "translation",
                mockNameModel,
                mockLocaleModel,
                mockTranslator,
                mockUserSessionFactory);

        when(mockUserSessionFactory.getUserSession()).thenReturn(mockUserSession);
        when(mockUserSession.getJcrSession()).thenReturn(mockHippoSession);
    }

    @Test
    public void execute_none_to_translate() throws Exception {
        // will test that if run with no languages to translate does not cause an error
        // should never happen but is a valid path through the code
        assertNull(action.execute(mockWorkflow));

        verify(mockTranslator).applyChangeSet(any(), any(), any());
    }

    @Test
    public void execute_with_changes() throws Exception {
        // when the change set list includes a ChangeSet they are passed to the DocumentTranslator
        ChangeSet change1 = mock(ChangeSet.class);
        ChangeSet change2 = mock(ChangeSet.class);
        changeSetList.add(change1);
        changeSetList.add(change2);

        action.execute(mockWorkflow);

        verify(mockTranslator).applyChangeSet(same(changeSetList), eq(mockHippoSession), eq(mockWorkflow));
    }

    @Test
    public void execute_with_error() throws Exception {
        ChangeSet change1 = mock(ChangeSet.class);
        changeSetList.add(change1);
        doThrow(new TranslationException("error message")).when(mockTranslator).applyChangeSet(
                same(changeSetList),
                eq(mockHippoSession),
                eq(mockWorkflow)
        );

        String message = action.execute(mockWorkflow);

        assertEquals("error message", message);
    }
}
