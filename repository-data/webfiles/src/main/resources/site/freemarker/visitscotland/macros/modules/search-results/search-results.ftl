<#include "../../../../include/imports.ftl">
<#include "../../../../frontend/components/vs-container.ftl">
<#include "../../../../frontend/components/vs-row.ftl">
<#include "../../../../frontend/components/vs-col.ftl">

<#macro searchResults>
    <div class="container">
        <div id="cludo-search-results">
            <div class="row">
                <div class="col-12 cludo-c-3">
                    <div class="search-result-count"></div>
                    <div class="search-filters" role="navigation"></div>
                </div>
                
                <div class="col-12 cludo-c-9" role="main">
                    <div class="search-did-you-mean"></div>
                    <div class="search-results"></div>
                </div>
            </div>
        </div>
    </div>
</#macro>