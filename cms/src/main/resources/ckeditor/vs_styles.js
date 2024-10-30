(function () {
    "use strict";

    CKEDITOR.stylesSet.add('article', [
        {
            element: 'h3',
            name: 'Heading'
        },
        {
            element: 'h6',
            name: 'Subheading'
        }
    ]);

    CKEDITOR.stylesSet.add('content_list', [
        {
            element: 'h6',
            name: 'Subheading'
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
        }
        /* H6 styles would be transformed to h4 in HTMLtoVueTransformer */
    ]);

    CKEDITOR.stylesSet.add('general_intro', [
        {
           element: 'span',
           name: 'Info Text',
           attributes: { 'class': 'info-text' }
        },
    ]);
}());
