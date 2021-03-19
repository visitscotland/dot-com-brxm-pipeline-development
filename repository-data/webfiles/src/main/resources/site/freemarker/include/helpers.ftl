<#--
    Functions
-->

<#-- @ftlvariable name="ResourceBundle" type="com.visitscotland.brxm.services.ResourceBundleService" -->

<#--  More reliable method for including labels from resource bundles  -->
<#--  Usage: ${label("essentials.global", "footer.signup")} -->
<#function label bundle key>
    <#if ResourceBundle??>
        <#return ResourceBundle.getResourceBundle(bundle, key, locale, false)>
    <#else>
        <#return labelFallback(bundle, key)>
    </#if>
</#function>

<#--  More reliable method for including labels from resource bundles  -->
<#--  Usage: ${label("essentials.global", "footer.signup")} -->
<#function optionalLabel bundle key>
    <#if ResourceBundle??>
        <#return ResourceBundle.getResourceBundle(bundle, key, locale, true)>
    <#else>
        <#return labelFallback(bundle, key)>
    </#if >
</#function>

<#-- Fallback mechanism for requesting a label. It should not be used outside of this File -->
<#-- e.g. DO NOT USE THIS METHOD -->
<#function labelFallback bundle key >
    <@hst.setBundle basename="${bundle}"/>
    <@fmt.message var="message" key="${key}" />
    <@log "ResourceBundle object not defined in the template" />
    <#return message>
</#function>

<#--  Logs an error in the FreeMarker Console  -->
<#--  It is a hacky way of logging an error and It is achieved by invoking a method that does not exist -->
<#--  Usage: ${log("Oh no! Something is not all right")} -->
<#function log message>
    console.error(message)
</#function>

<#function productSearch locale productType lat lon proximity>
<#--TODO: proximity as optional-->
    <#return ProductSearchBuilder.newInstance().productTypes(productType).proximity(proximity).coordinates(lat, lon).build()>
</#function>

<#--  Get correct URL for internal or external link -->
<#--  Usage: ${getUrl(navItem)} -->
<#function getUrl item>
    <#if item.hstLink??>
        <#assign href><@hst.link fullyQualified=fullyQualifiedURLs link=item.hstLink/></#assign>
        <#return href> 
    <#elseif item.externalLink??>
        <#return item.externalLink?replace("\"", "")> 
    <#else>
        <#return "#"> 
    </#if>
</#function>



<#--TODO: polimorphism-->
<#--<#function productSearch locale productType location >-->
    <#--<#return ProductSearchBuilder.newInstance().productType(productType).locale(locale).location(location).build()>-->
<#--</#function>-->