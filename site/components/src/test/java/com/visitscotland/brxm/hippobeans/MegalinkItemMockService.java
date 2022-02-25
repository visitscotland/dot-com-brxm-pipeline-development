package com.visitscotland.brxm.hippobeans;

import org.easymock.EasyMock;

public class MegalinkItemMockService {

    public MegalinkItem createMock(boolean featured) {
        MegalinkItem item = EasyMock.createMock(MegalinkItem.class);

        EasyMock.expect(item.getFeature()).andReturn(featured).anyTimes();
        EasyMock.expect(item.getLinkItem()).andReturn(mockCMSLink()).anyTimes();

        EasyMock.replay(item);

        return item;
    }

    Page mockCMSLink(){
        Page page = EasyMock.createNiceMock(Page.class);
        Image image = EasyMock.createNiceMock(Image.class);

        EasyMock.expect(page.getTitle()).andReturn("Linked page").anyTimes();
        EasyMock.expect(page.getTeaser()).andReturn("Teaser of the linked page").anyTimes();
        EasyMock.expect(page.getHeroImage()).andReturn(image).anyTimes();

        EasyMock.replay(image, page);

        return page;
    }
}
