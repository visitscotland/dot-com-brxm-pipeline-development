package com.visitscotland.brxm.hippobeans;

import org.easymock.EasyMock;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.ArrayList;
import java.util.List;

public class MegalinksMockService {

    private final MegalinkItemMockService itemService;

    public MegalinksMockService(){
        itemService = new MegalinkItemMockService();
    }

    public Megalinks createMock(String title, boolean introduction, boolean listView, boolean teaser, int links, String intro){
        Megalinks mega = EasyMock.createNiceMock(Megalinks.class);
        SingleImageModule single = EasyMock.createNiceMock(SingleImageModule.class);
        Image img = EasyMock.createNiceMock(Image.class);
        List<MegalinkItem> items = new ArrayList<>();

        EasyMock.expect(mega.getTitle()).andReturn(title).anyTimes();
        if (listView) {
            EasyMock.expect(mega.getLayout()).andReturn("list").anyTimes();
        }else{
            EasyMock.expect(mega.getLayout()).andReturn("default").anyTimes();
        }
        EasyMock.expect(mega.getTeaserVisible()).andReturn(teaser).anyTimes();

        EasyMock.expect(mega.getSingleImageModule()).andReturn(single).anyTimes();

        EasyMock.expect(single.getTitle()).andReturn(intro);
        EasyMock.expect(single.getImage()).andReturn(img);
        EasyMock.expect(single.getIntroduction()).andReturn(EasyMock.createNiceMock(HippoHtml.class));

        if (introduction){
            EasyMock.expect(mega.getIntroduction()).andReturn(EasyMock.createNiceMock(HippoHtml.class)).anyTimes();
        } else {
            EasyMock.expect(mega.getIntroduction()).andReturn(null).anyTimes();
        }

        for (int i=0; i<links; i++) {
            items.add(itemService.createMock(false));
        }

        EasyMock.expect(mega.getMegalinkItems()).andReturn(items).anyTimes();

        EasyMock.replay(mega, single, img);

        return mega;
    }

    public Megalinks createMock(String title){
        return createMock(title, title!=null,true, true, 0);
    }


    public Megalinks createMock(String title, boolean introduction, boolean listView, boolean teaser, int links){
        Megalinks mega = EasyMock.createNiceMock(Megalinks.class);
        List<MegalinkItem> items = new ArrayList<>();

        EasyMock.expect(mega.getTitle()).andReturn(title).anyTimes();
        if (listView) {
            EasyMock.expect(mega.getLayout()).andReturn("list").anyTimes();
        }else{
            EasyMock.expect(mega.getLayout()).andReturn("default").anyTimes();
        }
        EasyMock.expect(mega.getTeaserVisible()).andReturn(teaser).anyTimes();
        EasyMock.expect(mega.getSingleImageModule()).andReturn(null).anyTimes();

        if (introduction){
            EasyMock.expect(mega.getIntroduction()).andReturn(EasyMock.createNiceMock(HippoHtml.class)).anyTimes();
        } else {
            EasyMock.expect(mega.getIntroduction()).andReturn(null).anyTimes();
        }

        for (int i=0; i<links; i++) {
            items.add(itemService.createMock(false));
        }

        EasyMock.expect(mega.getMegalinkItems()).andReturn(items).anyTimes();

        EasyMock.replay(mega);

        return mega;
    }


}
