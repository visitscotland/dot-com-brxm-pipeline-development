<#compress>
    <#include "../../../include/imports.ftl">
    <#include "../footerContributions.ftl">
    <#include "../base-footer.ftl">
    <#include "../base-top-menu.ftl">
    <#include "../utility-footer.ftl">

    <#assign integration=true>

    <@footerContributions integration/>

    <script>
        (function() {
            const footerSrc = document.querySelector('[data-js-footer-source]');
            const footerDest = document.querySelector('[data-js-footer-dest]');

            footerDest.innerHTML = footerSrc.innerHTML;
            footerSrc.innerHTML = '';

            const accordionPanels = footerDest.getElementsByClassName('vs-accordion-item__panel');

            for (let i = 0; i < accordionPanels.length; i++) {
                accordionPanels[i].classList.remove('d-md-block');
                accordionPanels[i].classList.add('d-block');
            }

        })();
    </script>
    <!-- end include -->
</#compress>
