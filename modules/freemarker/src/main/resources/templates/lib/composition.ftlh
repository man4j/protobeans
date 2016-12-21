<#macro composition templateName>
  <#nested />
  <#include templateName />
</#macro>

<#macro insert value>
  <#if value?has_content>
    <#if value?markup_string?ends_with(".ftlh")>
      <#include value /><#t />
    <#else>
      ${value?no_esc}<#t />
    </#if>
  </#if>
</#macro>
