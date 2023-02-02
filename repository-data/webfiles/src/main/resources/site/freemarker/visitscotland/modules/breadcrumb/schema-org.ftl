<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="breadcrumb" type="org.onehippo.forge.breadcrumb.om.Breadcrumb" -->

<!-- Note: Translated labels have not been created because this scrip should only be added for the English version -->
<#if Properties.siteAboutUs == hstRequest.request.pathInfo && language = 'en'>
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
                    "${label('navigation.social-media', 'facebook')}",
                    "${label('navigation.social-media', 'twitter')}",
                    "${label('navigation.social-media', 'youtube')}",
                    "${label('navigation.social-media', 'instagram')}"
                ]
            }
        </script>
    </@hst.headContribution>
</#if>

