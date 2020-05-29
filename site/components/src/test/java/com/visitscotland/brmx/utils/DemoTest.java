package com.visitscotland.brmx.utils;

import com.visitscotland.brmx.beans.MegaLinks;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.soap.Node;

public class DemoTest {

    @Test
    void mockDemo(){
        final String TITLE = "Megalink title";

        //Create a Mock for the class MegaLinks
        MegaLinks mega = EasyMock.createNiceMock(MegaLinks.class);
        //Create a rule for the Mock
        EasyMock.expect(mega.getTitle()).andReturn(TITLE);
        //Applies the rules to the mock
        EasyMock.replay(mega);
        //Execute the method
        Assertions.assertEquals(TITLE, mega.getTitle());
    }


}
