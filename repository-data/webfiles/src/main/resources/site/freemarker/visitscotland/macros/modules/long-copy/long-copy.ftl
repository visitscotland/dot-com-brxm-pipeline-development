<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-image-with-caption.ftl">
<#include "../../global/image-with-caption.ftl">

<#-- @ftlvariable name="module" type="com.visitscotland.brxm.model.LongCopyModule" -->

<#macro longCopy module>

    <vs-col cols="12" lg="8" offset-lg="2" style="border: 1px solid grey;">
        <@hst.html hippohtml=module.copy/>
    </vs-col>

    </br> </br>
</#macro>