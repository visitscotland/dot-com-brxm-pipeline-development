<#compress>
    <#include "../visitscotland/functions/helpers.ftl">
    <#include "../visitscotland/macros/shared/logger.ftl">

    <#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >
    <#assign fmt=JspTaglibs ["http://java.sun.com/jsp/jstl/fmt"] >

    <@hst.defineObjects />

    <#-- @ftlvariable name="hstRequestContext" type="org.hippoecm.hst.core.request.HstRequestContext" -->

    <#assign locale =hstRequestContext.resolvedMount.mount.locale?replace("_","-")?lower_case>
    <#assign language = locale?keep_before("-")>

    <#-- Indicates if the URLs need to be fully qualified. (i.e. For integration with 3rd parties) -->
    <#assign fullyQualifiedURLs = hstRequestContext.getModel("fullyQualified")???then(true,false)>
    <#assign integration=false>
</#compress>