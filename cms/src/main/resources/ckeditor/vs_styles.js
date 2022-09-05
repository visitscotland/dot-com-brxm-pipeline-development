(function () {
    "use strict";

    CKEDITOR.stylesSet.add('article', [
        {
            element: 'h3',
            name: 'H3'
        },
        {
            element: 'h6',
            name: 'H4'
        }
    ]);

    CKEDITOR.stylesSet.add('long_copy', [
        {
            element: 'h2',
            name: 'H2'
        },
        {
            element: 'h3',
            name: 'H3'
        },
        {
            element: 'h4',
            name: 'H4'
        },
        {
            element: 'h5',
            name: 'H5'
        },
        {
            element: 'h6',
            name: 'H6'
        }
    ]);

    CKEDITOR.stylesSet.add('general_intro', [
        {
           element: 'span',
           name: 'Info Text',
           attributes: { 'class': 'info-text' }
        },
    ]);
}());