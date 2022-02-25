/**
 * This is a helper util that sorts components
 * alphabetically.
 *
 * This is quite a hacky solution that really needs
 * to be refactored into proper rsg-components as
 * explained here:
 * https://vue-styleguidist.github.io/docs/Development.html#react-components
 *
 */

export default {
    methods: {
        sortList() {
            const tocList = document.querySelectorAll('div[class^="rsg--sidebar"] nav > ul > li > ul ');

            if (tocList) {
                Array.from(tocList).forEach((element) => {
                    Array.from(element.children)
                        .sort((a, b) => a.textContent.localeCompare(b.textContent))
                        .forEach((li) => element.appendChild(li));
                });
            }
        },
        init() {
            const sidebar = document.querySelector('div[class^="rsg--sidebar"]');

            if (sidebar) {
                this.sortList();
            }
        },
    },
    mounted() {
        this.init();
    },
};
