package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.api.HippoSession;

import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SameNameSiblingProvider implements IDataProvider<SameNameSiblingProvider.Entry> {
    private List<Entry> entryList;

    public SameNameSiblingProvider(List<ChangeSet> changeSetList, HippoSession jcrSession) throws RepositoryException {
        this.entryList = new ArrayList<>();

        for (ChangeSet changeSet : changeSetList) {
            ILocaleProvider.HippoLocale targetLocale = changeSet.getTargetLocale();

            for (FolderTranslation folder : changeSet.getFolders()) {
                entryList.addAll(createSameNameSiblingEntries(targetLocale, jcrSession, folder));
            }

            for (FolderTranslation document : changeSet.getDocuments()) {
                entryList.addAll(createSameNameSiblingEntries(targetLocale, jcrSession, document));
            }
        }
    }

    protected List<Entry> getEntryList() {
        return entryList;
    }

    @Override
    public Iterator<? extends Entry> iterator(long first, long count) {
        return entryList.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return entryList.size();
    }

    @Override
    public IModel<Entry> model(Entry object) {
        return new LoadableDetachableModel<Entry>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected Entry load() {
                return object;
            }

        };
    }

    @Override
    public void detach() {

    }

    protected List<Entry> createSameNameSiblingEntries(ILocaleProvider.HippoLocale locale,
                                                       HippoSession jcrSession,
                                                       FolderTranslation folderTranslation) throws RepositoryException {
        List<Entry> sameNameSiblingEntries = new ArrayList<>();
        if (folderTranslation.hasSameNameSibling() && folderTranslation.hasSameUrlSibling() &&
                folderTranslation.getSameUrlSiblingId().equals(folderTranslation.getSameNameSiblingId())) {
            HippoNode sibling = (HippoNode) jcrSession.getNodeByIdentifier(folderTranslation.getSameUrlSiblingId());
            sameNameSiblingEntries.add(new Entry(locale, sibling.getName(),
                    sibling.getDisplayName(), true, true));
        } else {
            if (folderTranslation.hasSameUrlSibling()) {
                HippoNode sibling = (HippoNode) jcrSession.getNodeByIdentifier(folderTranslation.getSameUrlSiblingId());
                sameNameSiblingEntries.add(new Entry(locale, sibling.getName(),
                        sibling.getDisplayName(), false, true));
            }

            if (folderTranslation.hasSameNameSibling()) {
                HippoNode sibling = (HippoNode) jcrSession.getNodeByIdentifier(folderTranslation.getSameNameSiblingId());
                sameNameSiblingEntries.add(new Entry(locale, sibling.getName(),
                        sibling.getDisplayName(), true, false));
            }
        }

        return sameNameSiblingEntries;
    }

    public class Entry {
        private ILocaleProvider.HippoLocale locale;
        private String url;
        private String name;
        private boolean sameNameSibling;
        private boolean sameUrlSibling;

        protected Entry(ILocaleProvider.HippoLocale locale,
                        String url,
                        String name,
                        boolean sameNameSibling,
                        boolean sameUrlSibling) {
            this.locale = locale;
            this.url = url;
            this.name = name;
            this.sameNameSibling = sameNameSibling;
            this.sameUrlSibling = sameUrlSibling;
        }

        public ILocaleProvider.HippoLocale getLocale() {
            return locale;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        public boolean hasSameNameSibling() {
            return sameNameSibling;
        }

        public boolean hasSameUrlSibling() {
            return sameUrlSibling;
        }
    }
}
