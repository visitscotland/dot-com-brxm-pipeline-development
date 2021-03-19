package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hippoecm.frontend.translation.ILocaleProvider;

import java.util.*;

public class DocumentChangeProvider implements IDataProvider<DocumentChangeProvider.Entry> {
    protected List<Entry> documentChangeList;
    private TypeNameFactory typeNameFactory;

    public DocumentChangeProvider(List<ChangeSet> changeSetList) {
        this(changeSetList, new TypeNameFactory());
    }

    protected DocumentChangeProvider(List<ChangeSet> changeSetList, TypeNameFactory typeNameFactory) {
        this.typeNameFactory = typeNameFactory;
        documentChangeList = buildDocumentChangeMap(changeSetList);
    }

    protected List<Entry> buildDocumentChangeMap(List<ChangeSet> changeSetList) {
        // Need to convert the List<ChangeSet> into a list of unique document names and the languages they will
        // cloned into
        Map<String, Entry> documentEntryMap = new LinkedHashMap<>();
        for (ChangeSet change : changeSetList) {
            ILocaleProvider.HippoLocale targetLocale = change.getTargetLocale();
            for (FolderTranslation document : change.getDocuments()) {
                Entry documentEntry = documentEntryMap.get(document.getName());
                if (null == documentEntry) {
                    documentEntry = new Entry(document.getName(),
                            typeNameFactory.lookupTypeName(document.getSourceNode()));
                    documentEntryMap.put(document.getName(), documentEntry);
                }
                documentEntry.addLocale(targetLocale);
            }
        }
        return new ArrayList<>(documentEntryMap.values());
    }

    @Override
    public Iterator<? extends Entry> iterator(long first, long count) {
        return documentChangeList.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return documentChangeList.size();
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

    public class Entry {
        private String documentName;
        private String documentType;
        private List<ILocaleProvider.HippoLocale> localeList;

        Entry(String documentName, String documentType) {
            this.documentName = documentName;
            this.localeList = new ArrayList<>();
            this.documentType = documentType;
        }

        public String getDocumentName() {
            return documentName;
        }

        protected void addLocale(ILocaleProvider.HippoLocale toAdd) {
            localeList.add(toAdd);
        }

        public List<ILocaleProvider.HippoLocale> getLocaleList() {
            return localeList;
        }

        public String getDocumentType() {
            return documentType;
        }
    }
}
