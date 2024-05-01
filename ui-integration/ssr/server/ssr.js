const cheerio = require("cheerio");
const { html: beautifyHtml } = require("js-beautify");

const { VsSSR } = require('@visitscotland/component-library');

const hydrationAttributeName = "data-vue-hydration-init";
const hydrationPlaceholderAttrName = "vue-template-outlet";
const hydrationPlaceholderHtml = `<span ${hydrationPlaceholderAttrName}></span>`;
const appAttributeName = "data-vue-app-init";
const templatePlaceholderAttrName = "vue-ssr-outlet";
const templatePlaceholderHtml = `<span ${templatePlaceholderAttrName}></span>`;
const xTemplateId = "app-template";

const parsePageParts = (pageHtml) => {
    const $page = cheerio.load(pageHtml, {
        script: true,
        style: true,
        pre: true,
    });

    const $appNode = $page(`[${appAttributeName}]`).replaceWith(templatePlaceholderHtml);

    $page(`[${hydrationAttributeName}]`).after(hydrationPlaceholderHtml);

    if (!$appNode) {
        throw new Error("App element missing");
    }

    return {
        $page,
        $appNode,
    }
}

const formatHtml = (subjectHtml) => beautifyHtml(
    subjectHtml,
    {
		indent_with_tabs: false,
        preserve_newlines: false,
        indent_char: "",
    },
);

const prepSsrTemplate = ($page, formattedAppHtml) => {
    const xTemplateHtml = makeXTemplate(formattedAppHtml);

    $page(`[${hydrationPlaceholderAttrName}]`).replaceWith(xTemplateHtml);

    $template = $page;
}

// inserts pre-rendered app template into an x-template
const makeXTemplate = (template) => {
    // the data-vue-app-init must be removed otherwise the app will be instantiated twice
    return `
        <script type="text/x-template" id="${xTemplateId}">
            ${template.replace(" data-vue-app-init", "")}
        </script>
    `;
}

const completeSsrTemplate = (appHtml) => {
    $template(`[${templatePlaceholderAttrName}]`).replaceWith(appHtml);

    // const pageHtml = transformHtml($template.html());
    let pageHtml = $template.html();

    // The app must be initiated in SSR mode for it to hydrate already rendered
    // vue code
    pageHtml = pageHtml.replaceAll('SSRed = false', 'SSRed = true');

    return pageHtml;

    // Formatting the HTML here breaks hydration due to node tree mismatch
    // TODO: Enable formatting without breaking hydration
    // return formatHtml(pageHtml)
}

const renderPage = async (pageHtml) => {
    const { $appNode, $page } = parsePageParts(pageHtml)

    // HTML is formatted before rendering to ensure successful hydration
    let formattedAppHtml = cheerio.html($appNode, { decodeEntities: false })

    prepSsrTemplate($page, formattedAppHtml);

    const app = VsSSR.createSSRApp({
        template: formattedAppHtml,
    });

    return VsSSR.renderToString(app).then((html) => {
        return completeSsrTemplate(html);
    });
}

module.exports = {
    renderPage,
}