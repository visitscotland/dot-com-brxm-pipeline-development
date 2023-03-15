<#include "../../functions/helpers.ftl">
<#--
    Both versions (script and noscript) are supposed to be included in every page. The implementation
    has been done in the same macro so all future ammends will be done in on single point.
-->
<#macro gtm noscript=false >
    <#assign id= property("gtm.container-id")>
    <#assign queryString = (property("gtm.is-production")?boolean)?then("", (property("gtm.preview-query-string"))) >

    <!-- Google Tag Manager -->
    <#if noscript >
        <noscript>
            <iframe src="https://www.googletagmanager.com/ns.html?id=${id}${queryString}" height="0" width="0" style="display:none;visibility:hidden"></iframe>
        </noscript>
    <#else>
        <script>
            (function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
                    new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
                j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
                'https://www.googletagmanager.com/gtm.js?id='+i+dl+ '${queryString}';f.parentNode.insertBefore(j,f);
            })(window,document,'script','dataLayer','${id}');
        </script>
    </#if>
    <!-- End Google Tag Manager -->
</#macro>