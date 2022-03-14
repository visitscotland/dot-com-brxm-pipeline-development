<#function themeCalculator  themeIndex="" module="" colourSchemeParam=["light", "light", "dark"]>
    <#if colourSchemeParam?size == 0>
        <#if breadcrumbs?? && breadcrumbs.items?size == 2>
            <!-- Note: There was a requirement about level 2 starting on light but It hasn't been either confirmed or discarded yet -->
            <!-- The following line allow to make the difference. Otherwise this if block can be simplified -->
            <#-- <#assign colourScheme = ["light", "light", "dark"]> -->
            <#assign colourScheme = ["dark", "light", "light"]>
        <#else>
            <#assign colourScheme = ["dark", "light", "light"]>
        </#if>
    <#else>
        <#assign colourScheme = colourSchemeParam>
    </#if>

    <#if themeIndex?has_content>
        <#return colourScheme[themeIndex]>
    <#elseif module?has_content && module.themeIndex?has_content>
        <#return colourScheme[module.themeIndex]>
    <#elseif module?has_content && module.getType() == "ICentreModule">
        <#return "grey">
    <#elseif module?has_content && module.getType() == "TravelInformationModule">
        <#return "dark">
    <#else>
        <#return "light">
    </#if>
</#function>
