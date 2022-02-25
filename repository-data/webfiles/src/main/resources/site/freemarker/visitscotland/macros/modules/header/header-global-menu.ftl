<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-global-menu.ftl">
<#include "../../../../frontend/components/vs-global-menu-language.ftl">
<#include "../../../../frontend/components/vs-global-menu-language-item.ftl">

<#-- @ftlvariable name="language" type="com.visitscotland.brxm.model.LocalizedURL"-->
<#-- @ftlvariable name="hstRequestContext" type="org.hippoecm.hst.core.request.HstRequestContext" -->

<#macro headerGlobalMenu>
    <#assign currentLocale=hstRequestContext.resolvedMount.mount.locale>
    <#if localizedURLs?size == 0 && hstRequestContext.getModel("placeholerLocalizedURLs")??>
        <#assign localizedURLs=hstRequestContext.getModel("placeholerLocalizedURLs")>
    </#if>

    <vs-global-menu
        dropdown-label="${label('navigation.static', 'uninav.our-sites')}"
        active-site="https://www.visitscotland.com/"
    >
        <template slot="third-menu-item">
            <vs-global-menu-language 
                language="${currentLocale}"
                language-label="${label('navigation.static', 'universal.language')}"
            >
                <#list localizedURLs as language>
                    <vs-global-menu-language-item
                        key="${language.language}"
                        language-link="${language.url}"
                        language-name="${language.displayName}"
                        language="${language.locale.language}"
                    >
                    </vs-global-menu-language-item>
                </#list>
            </vs-global-menu-language>
        </template>
    </vs-global-menu>
</#macro>