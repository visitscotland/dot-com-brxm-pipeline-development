<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-footer-copyright.ftl">

<#macro footerCopyright>
    <vs-footer-copyright 
        href="<@hst.link fullyQualified=fullyQualifiedURLs siteMapItemRefId='root'/>"
        link-alt-text="${label('navigation.static', 'footer.logo-alt-text')}"
    >
        <span v-slot:copyright>
            ${label('navigation.static', 'footer.text')}
        </span>
    </vs-footer-copyright>
</#macro>