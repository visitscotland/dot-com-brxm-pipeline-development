<template>
    <form
        class="vs-form"
        :id="`mktoForm_${formId}`"
    />
</template>

<script>
/**
 * An icon list can be used where there is a list icons with a caption with optional heading.
 * An example use is to create a list of key facilities for a product.
 *
 * @displayName Form
 */

export default {
    name: 'VsForm',
    status: 'prototype',
    release: '0.0.1',
    props: {
        formId: {
            type: Number,
            required: true,
        },
    },
    mounted() {
        window.MktoForms2.loadForm('//e.visitscotland.com', '638-HHZ-510', this.formId);
        window.MktoForms2.whenRendered((form) => {
            this.destyleMktoForm(form);
        });
    },
    methods: {
        destyleMktoForm: (mktoForm, moreStyles) => {
            /*
            * @author Sanford Whiteman
            * @version v1.104
            * @license MIT License: This license must appear with all
            * reproductions of this software.
            *
            * Create a completely barebones, user-styles-only Marketo form
            * by removing inline STYLE attributes and disabling STYLE and LINK elements
            */
            const formEl = mktoForm.getFormElem()[0];
            const arrayify = getSelection.call.bind([].slice);

            const styledEls = arrayify(formEl.querySelectorAll('[style]')).concat(formEl);
            styledEls.forEach((el) => {
                el.removeAttribute('style');
            });
            const styleSheets = arrayify(document.styleSheets);
            styleSheets.forEach((ss) => {
                if ([window.mktoForms2BaseStyle, window.mktoForms2ThemeStyle]
                    .indexOf(ss.ownerNode) !== -1 || formEl.contains(ss.ownerNode)) {
                    ss.disabled = true; // eslint-disable-line no-param-reassign
                }
            });

            if (!moreStyles) {
                formEl.setAttribute('data-styles-ready', 'true');
            }
        },
    },

};
</script>

<style lang='scss'>
    .vs-form {
        input {
            border: 2px solid $color-theme-primary;
        }

        .mktoAsterix {
            display: none;
        }
    }
</style>
