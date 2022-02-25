package com.visitscotland.brxm.translation.plugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FolderTranslationTest {
    @Test
    public void isEditable_false() {
        // When the FolderTranslation is immutable exception thrown changing values
        FolderTranslation folderTranslation = new FolderTranslation("id");
        folderTranslation.setEditable(false);

        assertThrows(UnsupportedOperationException.class, () -> folderTranslation.setNamefr("nameFr"));
        assertThrows(UnsupportedOperationException.class, () -> folderTranslation.setUrlfr("urlFr"));
    }

    @Test
    public void isEditable_true() {
        // No exception thrown when editable
        FolderTranslation folderTranslation = new FolderTranslation("id");
        folderTranslation.setEditable(true);

        folderTranslation.setNamefr("nameFr");
        assertEquals("nameFr", folderTranslation.getNamefr());
        folderTranslation.setUrlfr("urlFr");
        assertEquals("urlFr", folderTranslation.getUrlfr());
    }

    @Test
    public void constructor() {
        FolderTranslation folderTranslation = new FolderTranslation("id");
        assertEquals("id", folderTranslation.getId());
    }


}
