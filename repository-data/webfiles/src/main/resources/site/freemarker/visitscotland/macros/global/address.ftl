<#macro address address sameLine=false>
    <#assign addressArr = [
        address.line1!"",
        address.line2!"",
        address.line3!"",
        address.city!"",
        address.postCode!""
    ]/>

<#--
    Filter out empty strings in address

    It would be tidier to do this within the loop below, but that
    causes <#sep> to incorrectly assume that the postCode is always
    a value that needs a comma before it, even if it is an empty
    string. The ideal solution would be to iterate over

    addressArr?filter()

    rather than constructing a whole filtered copy of the array
    for readability but that is not doable until we reach a future
    version of freemarker (2.3.29).

    TODO: Upgrade to freemarker version 2.3.29.
-->
    <#assign filterAddressArr = [] />
    <#list addressArr as addrLine>
        <#if addrLine != "">
            <#assign filterAddressArr = filterAddressArr + [ addrLine ] />
        </#if>
    </#list>
    <#list filterAddressArr as addressLine>
        <#if sameLine>
            ${addressLine?eval}<#sep>,
        <#else>
            <span>${addressLine?eval}<#sep>,</span>
        </#if>
    </#list>
</#macro>