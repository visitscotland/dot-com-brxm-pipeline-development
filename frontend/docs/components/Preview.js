import CodeMirror from "codemirror"
import CodeTabs from "../utils/tabs"
import Vue from "vue"

function format(node, level) {
    const indentBefore = new Array(level + 1).join("  ")
    const indentAfter = new Array(level - 1).join("  ")
    let textNode

    for (let i = 0; i < node.children.length; i += 1) {
        textNode = document.createTextNode(`\n${indentBefore}`)
        node.insertBefore(textNode, node.children[i])

        format(node.children[i], level + 1)

        if (node.lastElementChild === node.children[i]) {
            textNode = document.createTextNode(`\n${indentAfter}`)
            node.appendChild(textNode)
        }
    }

    return node
}

// https://vuejs.org/v2/guide/render-function.html
export default (previewComponent) => ({
    render(createElement) {
        return createElement(previewComponent)
    },
    created() {
        // Credit for fix - https://github.com/vue-styleguidist/vue-styleguidist/issues/770#issuecomment-633692858
        // Without this, the displayName is used as the component name and it breaks all existing instances where
        // the component is loaded (i.e. it makes <vs-header> no longer work, expecting <header> instead).

        Object.entries(Vue.options.components).forEach(c => {
            const displayName = c[0];
            const component = c[1];
            const { name } = component.extendOptions;

            // If display name is different than name, create an alias for the component
            // Ex: VsAlert component display name is Alert
            //     We then create VsAlert, an alias of Alert, to be used in preview
            if (displayName !== name) {
                Vue.component(name, component);
            }
        });
    },
    mounted() {
        CodeTabs.clean()
        const tabs = CodeTabs.create()
        const strDiv = this.$el.innerHTML.replace(/<!---->/g, "").replace(/data-v-\w*=""/g, "")
        const div = document.createElement("div")
        div.innerHTML = `<${
            this.$el.localName
        } class='${
            this.$el.className
        }'>${
            strDiv.trim()
        }</${
            this.$el.localName
        }>`

        const elemText = format(div, 1).innerHTML.replace(/ class=""/g, "")
        const elem = document.createElement("div")
        const pre = document.createElement("pre")
        const parent = document.querySelector("article div[class^='rsg--tab']")
        pre.appendChild(document.createTextNode(elemText.trim()))
        elem.appendChild(pre)
        if (parent) {
            // Allow some time to pass to make sure codemirror is visible first
            setTimeout(() => {
                parent.appendChild(elem)
                parent.appendChild(tabs)

                CodeMirror(
                    (code) => {
                        code.className += " vueds-html vueds-hidden" // eslint-disable-line no-param-reassign
                        elem.parentNode.replaceChild(code, elem)
                    },
                    {
                        value: pre.innerText || pre.textContent,
                        mode: "jsx",
                        lineNumbers: false,
                        lineWrapping: true,
                        readOnly: true,
                        dragDrop: false,
                        theme: "night",
                        viewportMargin: Infinity,
                    },
                )

                CodeTabs.init()
            }, 300)
        }
    },
})
