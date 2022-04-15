<#include "../../../../../include/imports.ftl">
<#include "../../../../../frontend/components/vs-mega-nav.ftl">
<#include "./header-nav.ftl">
<#include "./header-accordion-nav.ftl">

<#macro headerMegaNav menu=menu>
    <vs-mega-nav 
        href="<@hst.link fullyQualified=fullyQualifiedURLs siteMapItemRefId='root'/>"
        menu-toggle-alt-text="${label('navigation.static', 'meganav-toggle-btn-alt-text')}"
        search-button-text="${label('search', 'search')}"
        search-label-text="${label('search', 'search-label')}"
        search-clear-button-text="${label('search', 'clear-form')}"
        search-close-button-text="${label('search', 'close-form')}"
    >
        <template slot="megaNavTopMenuItems">
            <@headerDesktopNav menu=menu/>
        </template>

        <template slot="megaNavAccordionItems">
            <@headerAccordionNav menu=menu/>
        </template>
    </vs-mega-nav>

    <#assign language = locale?keep_before("-")>    

    <@hst.headContribution category="htmlBodyEndScripts">
        <script>
            var engine = {
				de: 8740,
				es: 8741,
				nl: 8744,
				fr: 8742,
				it: 8743,
				en: 8738,
			};

            document.addEventListener("DOMContentLoaded", function(){
                var CludoSearch;
                (function () {
                    var cludoSettings = {
                        customerId: 623,
                        engineId: engine["${language}"],
                        searchUrl: 'test/pages/search-results/content',
                        language: '${language}',
                        searchInputs: ['cludo-search-form'],
                        template: 'InlineBasicImages',
                        focusOnResultsAfterSearch: true,
                        type: 'inline'
                    };
                    CludoSearch = new Cludo(cludoSettings);
                    CludoSearch.init();
                })();
            });
        </script>
        <!--[if lte IE 9]>
            <script src="https://api.cludo.com/scripts/xdomain.js" slave="https://api.cludo.com/proxy.html" type="text/javascript"></script>
        <![endif]-->
    </@hst.headContribution>

    <@hst.headContribution category="htmlBodyEndScripts">
        <script type="text/javascript" src="https://customer.cludo.com/scripts/bundles/search-script.min.js"></script>
    </@hst.headContribution>
</#macro>