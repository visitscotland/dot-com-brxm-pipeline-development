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
            console.log('personalisation');

            function callbackFunction(response) {
                console.log(response);
                const personalisationContainers = document.querySelectorAll('[data-personalisation]');
                console.log(personalisationContainers);

                [...personalisationContainers].forEach(function(el) {
                    const personalisationSections = (el.querySelectorAll('[data-personalisation-type]'));

                    [...personalisationSections].forEach(function(section) {
                        if (section.dataset.personalisationType === response.results.location.country) {
                            section.classList.remove('personalisation--hidden');
                        }
                    });
                });
            }

            // fake api response
            <#--  const fakeApiResponse = {
                "status": 200,
                "results": {
                "org": "Virgin Media Limited",
                "location": {
                "country": "United States",
                "city": "Bartley",
                "state": null
                },
                "industries": [
                "Telecommunications"
                ],
                "isp": true,
                "matchedSegments": [],
                "abm": [],
                "category": "ENTERPRISE",
                "group": null
                }
            };  

            callbackFunction(fakeApiResponse); -->

            rtp('get', 'visitor', callbackFunction);
        </script>
    </@hst.headContribution>
</#macro>