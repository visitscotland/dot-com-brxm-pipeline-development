<#global incuded = {}>

<#macro includeOnce path>
  <#if incuded[path]??><#return></#if>
  <#include path>
  <#global incuded += {path: true}>
</#macro>