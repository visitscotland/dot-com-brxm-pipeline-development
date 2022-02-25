/**
 * This is Vue Design System’s helper util that
 * highlights the currently active nav item.
 *
 * This is quite a hacky solution that really needs
 * to be refactored into proper rsg-components as
 * explained here:
 * https://vue-styleguidist.github.io/docs/Development.html#react-components
 *
 * Alternatively, once CSS selectors level 4 is released
 * and makes parent selectors somehow available, the
 * vueds-active class will be redundant and all can be
 * replaced with a parent selector to get ancestors of
 * li[class*=\"rsg--isSelected\"]. See
 * https://stackoverflow.com/a/1014958/2018496
 *
 */

export default {
    methods: {
        clearActiveItems() {
            const activeItems = document.querySelectorAll(".vueds-active")
            if (activeItems) {
                ;[].forEach.call(activeItems, (element) => {
                    element.classList.remove("vueds-active")
                })
            }
        },
        activateItem(linkItem) {
            linkItem.classList.add("vueds-active")

            // Check for a grandparent nav link item
            const parent = linkItem.parentNode.parentNode

            if (parent && parent.className.match(/(rsg--item)/)) {
                this.activateItem(parent)
            }
        },
        refreshClickHandlers(element, index, navType) {
            if (this.navClickHandlers[navType][index]) {
                element.removeEventListener("click", this.navClickHandlers[navType][index], false)
            }

            this.navClickHandlers[navType][index] = this.click.bind(this)
            element.addEventListener("click", this.navClickHandlers[navType][index], false)
        },
        click(event) {
            if (this.clearActiveItems) {
                this.clearActiveItems()
            } else {
                this.methods.clearActiveItems()
            }

            this.activateItem(event.target.parentNode)
        },
        init() {
            const sidebar = document.querySelector("div[class^='rsg--sidebar']")

            if (sidebar) {
                if (this.clearActiveItems) {
                    this.clearActiveItems()
                } else {
                    this.methods.clearActiveItems()
                }

                const navLinks = sidebar.querySelectorAll("nav > ul > li > a")
                const subNavLinks = sidebar.querySelectorAll("nav > ul > li ul a")
                const search = sidebar.querySelector("div[class^='rsg--search'] input")
                const self = this
                const activeItem = sidebar.querySelector("li[class*=\"rsg--isSelected\"]")

                // init handler object
                if (!self.navClickHandlers) {
                    self.navClickHandlers = {
                        navLinks: {
                        },
                        subNavLinks: {
                        },
                    }
                }

                if (activeItem) {
                    this.activateItem(activeItem)
                }

                if (search && !search.classList.contains("set")) {
                    search.setAttribute("placeholder", "Type to filter")
                }

                // Cleanup

                if (navLinks) {
                    ;[].forEach.call(navLinks, (element, i) => {
                        self.refreshClickHandlers(element, i, "navLinks")
                    })
                }

                if (subNavLinks) {
                    ;[].forEach.call(subNavLinks, (element, i) => {
                        self.refreshClickHandlers(element, i, "subNavLinks")
                    })
                }
            }
        },
    },
    mounted() {
        this.init()
    },
}
