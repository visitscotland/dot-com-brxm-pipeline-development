<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">
<#include "../../../../frontend/components/vs-embed-wrapper.ftl">

<#macro searchResults>
    <div id="cludo-search-results" class="cludo-search-results">
        <div class="cludo-search-results__layout mb-9 mb-md-11">
            <vs-embed-wrapper>
                <template slot="embedIntroCopyNoJs">
                    <!-- Update this embed wrapper to use proper javascript fallback component and remove this hardcoded text  -->
                    JavaScript needs to be enabled to see search results. 
                </template>
                <template slot="embedWidget">
                    <div class="row">
                        <div class="col-12 col-lg-10 offset-lg-1 mb-4 mb-lg-8">
                            <div class="cludo-search-results__search-result-count search-result-count" role="status"></div>
                            <div class="cludo-search-results__did-you-mean search-did-you-mean" role="Complementary"></div>
                            <div class="cludo-search-results__facets search-filters" aria-controls="search-results"></div>
                        </div>
                        
                        <div class="col-12 col-lg-10 offset-lg-1" role="main">
                            <div class="cludo-search-results__results-wrapper">
                                <div class="cludo-search-results__results search-results" role="region" id="search-results" aria-live="polite"></div>
                            </div>
                        </div>
                    </div>
                </template>
            </vs-embed-wrapper>
        </div>
    </div>
</#macro>


