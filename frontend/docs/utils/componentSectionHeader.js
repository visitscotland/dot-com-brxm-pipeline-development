/**
 * This is a helper util to restyle the H1 on a section with
 * nested components on the page
 *
 */

export default {
    methods: {
        restyleComponentSectionHeader(heading1) {
            const components = document.querySelector('.section-components');
            const heading2 = document.querySelector('h2[class^="rsg--heading"]');

            // Check if the page also contains a H2
            // Add class to H1 if true
            if (this.hasH2Heading(heading2, components)) {
                heading1.classList.add('section-h1');
            }
        },
        hasH2Heading(node, parent) {
            return (node === parent) ? false : parent.contains(node);
        },
        init() {
            const heading1 = document.querySelector('h1[class^="rsg--heading"]');

            if (heading1) {
                this.restyleComponentSectionHeader(heading1);
            }
        },
    },
    mounted() {
        this.init();
    },
};
