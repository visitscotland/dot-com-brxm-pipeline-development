<#function themeCalculator  themeIndex=-1 module="" colourSchemeParam=["grey", "light", "light"]>
    <#if colourSchemeParam?size == 0>
        <#if breadcrumbs?? && breadcrumbs.items?size == 2>
            <!-- Note: There was a requirement about level 2 starting on light but It hasn't been either confirmed or discarded yet -->
            <!-- The following line allow to make the difference. Otherwise this if block can be simplified -->
            <#-- <#assign colourScheme = ["light", "light", "grey"]> -->
            <#assign colourScheme = ["grey", "light", "light"]>
        <#else>
            <#assign colourScheme = ["grey", "light", "light"]>
        </#if>
    <#else>
        <#assign colourScheme = colourSchemeParam>
    </#if>

    <#if themeIndex != -1>
        <#return colourScheme[themeIndex]>
    <#elseif module?has_content && module.themeIndex?has_content>
        <#return colourScheme[module.themeIndex]>
    <#elseif module?has_content && module.getType() == "ICentreModule">
        <#return "grey">
    <#else>
        <#return "light">
    </#if>
</#function>
