import vueServerRenderer from "vue-server-renderer";
import vueTemplateCompiler from "vue-template-compiler";

import cheerio from "cheerio";
import { beautifyHtml } from "js-beautify";

import buildMode from "../../build/base.build-mode";

import serverBundle from "../../dist/ssr/server/vue-ssr-server-bundle.json";
import clientManifest from "../../dist/ssr/client/vue-ssr-client-manifest.json";

// const transformHtml from "./transform-html";

const appAttributeName = "data-vue-app-init";
const templatePlaceholderAttrName = "vue-ssr-outlet";
const templatePlaceholderHtml = `<span ${templatePlaceholderAttrName}></span>`;
const xTemplateId = "app-template";

let $template;
let renderer;

const formatHtml = (subjectHtml) => beautifyHtml(
    subjectHtml,
    {
		indent_with_tabs: false,
        preserve_newlines: true,
        indent_char: "",
    },
);

const completeSsrTemplate = (appHtml) => {
    $template(`[${templatePlaceholderAttrName}]`).replaceWith(appHtml);

    // const pageHtml = transformHtml($template.html());
    return $template.html();

    // Formatting the HTML here breaks hydration due to node tree mismatch
    // TODO: Enable formatting without breaking hydration
    // return formatHtml(pageHtml)
}

const createRenderer = (bundle) => vueServerRenderer.createBundleRenderer(bundle, {
    runInNewContext: true,
    template: completeSsrTemplate,
    inject: true,
    clientManifest,
});

// inserts pre-rendered app template into an x-template
const makeXTemplate = (template) => {
    // the data-vue-app-init must be removed otherwise the app will be instantiated twice
    return `
        <script type="text/x-template" id="${xTemplateId}">
            ${template.replace(" data-vue-app-init", "")}
        </script>
    `;
}

const prepSsrTemplate = ($page, formattedAppHtml) => {
    const xTemplateHtml = makeXTemplate(formattedAppHtml);

    $page(`[${templatePlaceholderAttrName}]`).after(xTemplateHtml);

    $template = $page;
}

// Render the HTML page using Vue SSR on the provided app template
const renderApp = async (template) => {
    const compileOptions = {
        outputSourceRange: true,
    }

    const compiled = vueTemplateCompiler.ssrCompileToFunctions(template, compileOptions);

    const context = {
        vueOptions: {
            render: compiled.render,
            staticRenderFns: compiled.staticRenderFns,
        },
    };

    return renderer.renderToString(context);
}

const parsePageParts = (pageHtml) => {
    const $page = cheerio.load(pageHtml, {
        script: true,
        style: true,
        pre: true,
        comment: true,
    });

    const $appNode = $page(`[${appAttributeName}]`).replaceWith(templatePlaceholderHtml);

    if (!$appNode) {
        throw new Error("App element missing");
    }

    return {
        $page,
        $appNode,
    }
}

/**
 * Renders the Vue app contained in pageHTML
 * @param {String} pageHtml
 */
const renderPage = async (pageHtml) => {
    const { $appNode, $page } = parsePageParts(pageHtml)

    // HTML is formatted before rendering to ensure successful hydration
    const formattedAppHtml = formatHtml(cheerio.html($appNode, { decodeEntities: false }))

    prepSsrTemplate($page, formattedAppHtml);

    return renderApp(formattedAppHtml);
}

/**
 * Initialises the Vue SSR renderer
 * @param {*} nodeApp
 */
const initRenderer = (nodeApp) => {
    if (buildMode === "development") {
        // eslint-disable-next-line global-require
        require("./setup-dev-server")(nodeApp, (freshBundle) => {
            renderer = createRenderer(freshBundle);
        })
    } else {
        renderer = createRenderer(serverBundle);
    }
}

module.exports = {
    renderPage,
    initRenderer,
}
