package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.hippoecm.frontend.dialog.Dialog;
import org.hippoecm.frontend.plugins.standards.image.CachingImage;
import org.hippoecm.frontend.service.IconSize;
import org.hippoecm.frontend.translation.ILocaleProvider;

public class SameNameSiblingDialog extends Dialog {
    public static final String ID_IMG = "img";
    public static final String ID_DISPLAY_NAME = "display-name";
    public static final String ID_URL = "url";

    public SameNameSiblingDialog(SameNameSiblingProvider folderProvider) {
        setTitle(new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return "Same Name Sibling Exception";
            }
        });
        add(new DataView<SameNameSiblingProvider.Entry>("sibling", folderProvider) {
            {
                onPopulate();
            }

            @Override
            protected void populateItem(Item<SameNameSiblingProvider.Entry> item) {
                SameNameSiblingProvider.Entry sameNameSibling = item.getModelObject();
                ILocaleProvider.HippoLocale locale = sameNameSibling.getLocale();
                ResourceReference resourceRef = locale.getIcon(IconSize.M, ILocaleProvider.LocaleState.EXISTS);
                item.add(new CachingImage(ID_IMG, resourceRef));
                item.add(new Label(ID_DISPLAY_NAME, sameNameSibling.getName()) {
                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        if (sameNameSibling.hasSameNameSibling()) {
                            tag.put("class", "same-name-sibling");
                        }
                    }
                });
                item.add(new Label(ID_URL, sameNameSibling.getDisplayUrl()) {
                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        if (sameNameSibling.hasSameUrlSibling()) {
                            tag.put("class", "same-name-sibling");
                        }
                    }
                });
            }
        });
        setCancelVisible(false);
    }
}
