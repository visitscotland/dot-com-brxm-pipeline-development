<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="breadcrumb" type="org.onehippo.forge.breadcrumb.om.Breadcrumb" -->

<#if Properties.siteContactUs == hstRequest.request.pathInfo>
    <@hst.headContribution category="seo">
        <script type="application/ld+json">
            {
                "@context":"https://schema.org",
                "@type":"Organization",
                "name":"VisitScotland",
                "url":"https://www.visitscotland.com/",
                "logo":"https://sttc.visitscotland.com/static/img/logos/vs-logo-corp-360.png",
                "description": "VisitScotland.com is the official consumer website of VisitScotland, Scotland’s national tourist board.",
                "address": {
                    "@type": "PostalAddress",
                    "streetAddress": "Ocean Point One, 94 Ocean Drive",
                    "addressLocality": "Edinburgh",
                    "postalCode": "EH6 6JH",
                    "addressCountry": "Scotland"
                },
                "sameAs": [
                    "${optionalLabel('navigation.social-media', 'facebook')}",
                    "${optionalLabel('navigation.social-media', 'twitter')}",
                    "${optionalLabel('navigation.social-media', 'youtube')}",
                    "${optionalLabel('navigation.social-media', 'instagram')}"
                ]
            }
        </script>
    </@hst.headContribution>
</#if>

