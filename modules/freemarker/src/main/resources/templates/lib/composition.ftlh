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

<#macro addScript scriptName>
  <#if !names??>
    <#global names = {} />    
  </#if>
  
  <#if !scripts??>
    <#global scripts = [] />
  </#if>
  
  <#if !names[scriptName]??>
    <#assign script>
      <#nested />
    </#assign>

    <#global scripts = scripts + [script] />    
    <#global names = names + {scriptName : true} />
  </#if>
</#macro>

<#macro insertScripts>
  <#if scripts??>
    <#list scripts as script>
      ${script?no_esc}
    </#list>    
  </#if>
</#macro>
