<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">

<#macro introImage mainImage="" >
    <@hst.link var="imageSrc" hippobean=mainImage.cmsImage.original/>

    <vs-container class="mt-n4 mb-9">
        <vs-row>
            <vs-col
                class="col-12 col-lg-8 offset-lg-2"
            >
                <@imageWithCaption 
                    imageSrc=imageSrc
                    imageDetails=mainImage
                    variant="fullwidth"
                    isHero="false"
                    isVideo="false"
                />
            </vs-col>
        </vs-row>
    </vs-container>
</#macro>
