<#compress>
    <#include "../../../include/imports.ftl">

    <#assign integration=true>

    <#--    The main tag is opened in the header -->
        <div data-js-footer-dest class="notranslate">

        </div>
    </main>
    <#-- This  block cannot go in the script footer because it is language dependant. Our current Third
   Party header and footer Integration Specification suggests that scripts and css don't change between languages -->

    <!-- BEGIN HEAD CONTRIBUTIONS: htmlBodyEndScriptsFirst -->
    <@hst.headContributions categoryIncludes="htmlBodyLocalizedScripts" xhtml=true/>
    <!-- END HEAD CONTRIBUTIONS: htmlBodyEndScriptsFirst -->

    <!-- end include -->
</#compress>
