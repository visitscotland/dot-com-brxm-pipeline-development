package com.visitscotland.brxm.menu;

import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugins.cms.browse.list.DocumentListingPlugin;
import org.hippoecm.frontend.plugins.standards.DocumentListFilter;
import org.hippoecm.frontend.plugins.standards.list.DocumentsProvider;
import org.hippoecm.frontend.plugins.standards.list.TableDefinition;
import org.hippoecm.frontend.plugins.standards.list.datatable.SortState;

import javax.jcr.Node;

/**
 * Disables sorting of items in CMS folder views. All items are shown in the exact order they appear
 * in JCR repo
 */
public class UnsortableFolderViewPlugin extends DocumentListingPlugin<Node> {

    public UnsortableFolderViewPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);
    }

    @Override
    protected ISortableDataProvider<Node, String> newDataProvider() {
        return new DocumentsProvider(this.getModel(), new DocumentListFilter(this.getPluginConfig()), this.getTableDefinition().getComparators());
    }

    @Override
    protected TableDefinition<Node> newTableDefinition() {
        // Disables the sorting buttons on the column headings
        TableDefinition<Node> def = super.newTableDefinition();
        return new TableDefinition<>(def.getColumns(), def.showColumnHeaders(), false);
    }

    @Override
    protected boolean isOrderable() {
        return true;
    }

}
