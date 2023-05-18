const cheerio = require("cheerio");
const { renderToString } = require('@vue/server-renderer');
const { html: beautifyHtml } = require("js-beautify");

const { createSSRApp } = require('storybook-component-library');

const appAttributeName = "data-vue-app-init";
const templatePlaceholderAttrName = "vue-ssr-outlet";
const templatePlaceholderHtml = `<span ${templatePlaceholderAttrName}></span>`;
const xTemplateId = "app-template";

let app;

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

const formatHtml = (subjectHtml) => beautifyHtml(
    subjectHtml,
    {
		indent_with_tabs: false,
        preserve_newlines: true,
        indent_char: "",
    },
);

const prepSsrTemplate = ($page, formattedAppHtml) => {
    const xTemplateHtml = makeXTemplate(formattedAppHtml);

    $page(`[${templatePlaceholderAttrName}]`).after(xTemplateHtml);

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
    const pageHtml = $template.html();

    return pageHtml;

    // Formatting the HTML here breaks hydration due to node tree mismatch
    // TODO: Enable formatting without breaking hydration
    // return formatHtml(pageHtml)
}

const renderPage = async (pageHtml) => {
    const { $appNode, $page } = parsePageParts(pageHtml)

    // HTML is formatted before rendering to ensure successful hydration
    const formattedAppHtml = formatHtml(cheerio.html($appNode, { decodeEntities: false }))

    prepSsrTemplate($page, formattedAppHtml);

    app = createSSRApp({
        template: formattedAppHtml,
    });

    return renderToString(app).then((html) => {
        return completeSsrTemplate(html);
    });
}

module.exports = {
    renderPage,
}