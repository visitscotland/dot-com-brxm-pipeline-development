<#macro articleSide section>
    <#if section.image??>
        <#if section.image.cmsImage??>
            <#assign media>
                <@hst.link hippobean=section.image.cmsImage.original/>
            </#assign>
        <#else>
            <#assign media = section.image.externalImage!'' />
        </#if>
        <@imageWithCaption imageSrc=media imageDetails=section.image variant="fullwidth"/>
    </#if>
    <#if section.quote??>
        <#-- TODO: Resuse code.
            This same code will be used for iCentre Module.
            Please, refactor in a way that makes sense.
        -->
        <#if section.quote.image.cmsImage??>
            <#assign imageQuote>
                <@hst.link hippobean=section.quote.image.cmsImage.thumbnail/>
            </#assign>
        <#else>
            <#assign imageQuote = section.quoteImage.externalImage!'' />
        </#if>

        <#assign realQuote>

        </#assign>


        <vs-row>
            <vs-col cols="1" >
                <vs-heading level="1">&ldquo;</vs-heading>
            </vs-col>
            <vs-col cols="9" >
                <@hst.html hippohtml=section.quote.quote />
            </vs-col>
            <vs-col cols="1" >
                <vs-heading level="1">&rdquo;</vs-heading>
            </vs-col>
        </vs-row>



            <br>

            <vs-row>
                <#if imageQuote?? && imageQuote?has_content>
                <vs-col cols="3" offset-lg="1">
                    <vs-img alt="${(section.quote.image)!'${label("essentials.global", "default.alt-text")}'}"
                            src="${imageQuote}">
                    </vs-img>
                </vs-col>
                </#if>
                <vs-col cols="8" >
                    <vs-heading level="6">${section.quote.authorName}</vs-heading>
                    ${section.quote.authorTitle}
                </vs-col>
            </vs-row>


    </#if>
</#macro>