<#macro personalisation>
    <@hst.headContribution category="htmlBodyEndScripts">
        <!-- Marketo RTP tag --> 
        <script type='text/javascript'>
        (function(c,h,a,f,i,l,e){c[a]=c[a]||function(){(c[a].q=c[a].q||[]).push(arguments)};
        c[a].a=i;c[a].l=l;c[a].e=e;var g=h.createElement("script");g.async=true;g.type="text/javascript";
        g.src=f+'?aid='+i;var b=h.getElementsByTagName("script")[0];b.parentNode.insertBefore(g,b);
        })(window,document,"rtp","//lonrtp1-cdn.marketo.com/rtp-api/v1/rtp.js","visitscotland1");
        
        rtp('send','view');
        rtp('get', 'campaign',true);
        </script>
        <!-- End of RTP tag -->
    </@hst.headContribution>
    <@hst.headContribution category="htmlBodyEndScripts">
        <script type="text/javascript">
            function callbackFunction(response) {
                const personalisationContainers = document.querySelectorAll('[data-personalisation]');

                [...personalisationContainers].forEach(function(el) {
                    const personalisationSections = (el.querySelectorAll('[data-personalisation-type]'));

                    let personalisationMatch = false;
                    [...personalisationSections].forEach(function(section) {
                        if (section.dataset.personalisationType === response.results.location.country) {
                            personalisationMatch = true;
                            section.classList.remove('personalisation--hidden');
                        }
                    });

                    if (!personalisationMatch) {
                        const defaultEl = el.querySelectorAll('[data-personalisation-type="default"]')[0];
                        defaultEl.classList.remove('personalisation--hidden');
                    }
                });
            }

            rtp('get', 'visitor', callbackFunction);
        </script>
    </@hst.headContribution>
</#macro>