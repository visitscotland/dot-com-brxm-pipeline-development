<#macro devEnvMenu>
    <div class="bg-dark">
        <div class="d-flex flex-column flex-lg-row justify-content-lg-between container-lg py-2">
            <span class="text-warning"><strong>Branch:</strong> ${ciBranch}</span>
            <span class="text-warning"><strong>Author:</strong> ${ciCommitAuthor}</span>
            <#if ciPrID??>
                <span class="text-warning"><strong>PR:</strong> ${ciPrID}</span>
            </#if>
        </div>
    </div>
</#macro>