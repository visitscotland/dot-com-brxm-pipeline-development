<#include "../../../../include/imports.ftl">
<#include "../carousel/carousel.ftl">
<#include "../../../../frontend/components/vs-module-wrapper.ftl">
<#include "../../global/preview-warning.ftl">

<#macro horizontalList item themeName="" testId="">
    <@previewWarning editMode item item.errorMessages/>

    <vs-module-wrapper
        theme="<#if themeName?has_content>${themeName}<#else>light</#if>"
        data-test="<#if testId?has_content>${testId}<#else>vs-otyml</#if>"
    >
        <template v-slot:vs-module-wrapper-heading>
            ${item.title}
        </template>

        <template v-slot:vs-module-wrapper-intro>
            <@hst.html hippohtml=item.introduction/>
        </template>

        <@carousel item />
    </vs-module-wrapper>
</#macro>


