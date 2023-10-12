<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-footer-social-menu.ftl">
<#include "../../../../frontend/components/vs-footer-social-item.ftl">
<#include "../../../../frontend/components/vs-list.ftl">

<#macro footerSocialMenu>
    <vs-footer-social-menu>
        <template v-slot:title>
            ${label('navigation.static', 'footer.find-us-on')}
        </template>

        <vs-footer-social-item
            href="${optionalLabel('navigation.social-media', 'facebook')}"
            icon="facebook"
        ></vs-footer-social-item>
        <vs-footer-social-item
            href="${optionalLabel('navigation.social-media', 'twitter')}"
            icon="twitter"
        ></vs-footer-social-item>
        <vs-footer-social-item
            href="${optionalLabel('navigation.social-media', 'youtube')}"
            icon="youtube"
        ></vs-footer-social-item>
        <vs-footer-social-item
            href="${optionalLabel('navigation.social-media', 'instagram')}"
            icon="instagram"
        ></vs-footer-social-item>
    </vs-footer-social-menu>
</#macro>