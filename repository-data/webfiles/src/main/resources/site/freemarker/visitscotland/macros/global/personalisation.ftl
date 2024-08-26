<#macro personalisation>
    <#if (!editMode) >
    <@hst.headContribution category="htmlBodyEndScripts">
        <script type="text/javascript">
            function callbackFunction(response) {
                const personalisationContainers = document.querySelectorAll('[data-personalisation]');

                [...personalisationContainers].forEach(function(el) {
                    const personalisationSections = (el.querySelectorAll('[data-personalisation-type]'));
                    const defaultEl = el.querySelectorAll('[data-personalisation-type="default"]')[0];

                    let personalisationMatch = false;
                    [...personalisationSections].forEach(function(section) {
                        if (section.dataset.personalisationType === response.results.location.country) {
                            personalisationMatch = true;
                            section.classList.remove('personalisation--hidden');
                            defaultEl.classList.add('personalisation--hidden');
                        }
                    });
                });
            }

            rtp('get', 'visitor', callbackFunction);
        </script>
    </@hst.headContribution>
    </#if>
</#macro>