<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="breadcrumb" type="org.onehippo.forge.breadcrumb.om.Breadcrumb" -->

<!-- Note: Translated labels have not been created because this script should only be added for the English version -->
<#if Properties.siteAboutUs == hstRequest.request.pathInfo && language = 'en'>
    <@hst.headContribution category="seo">
        <script type="application/ld+json">
            {
                "@context":"https://schema.org",
                "@type":"Organization",
                "name":"${label('seo', 'site-name')}",
                "url":"https://www.visitscotland.com/",
                "logo":"${label('seo', 'vs-logo')}",
                "description": "${label('seo', 'organization.description')}",
                "address": {
                    "@type": "PostalAddress",
                    "streetAddress": "${label('seo', 'organization.streetAddress')}",
                    "addressLocality": "${label('seo', 'organization.addressLocality')}",
                    "postalCode": "${label('seo', 'organization.postalCode')}",
                    "addressCountry": "${label('seo', 'organization.addressCountry')}"
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

