<#macro ifIsCurrentUri uri what="" other="">
  <#assign checkedUrl = rc.getContextUrl(uri) />
  
  <#if rc.requestUri == checkedUrl>
    ${what}
  <#else>
    ${other}
  </#if>
</#macro>

<#macro r path>
  <@f.url '${getBean("freeMarkerVariables").getVariable("resourcesPrefix")}/${path}' />
</#macro>

<#function getBean name>
  <#return rc.webApplicationContext.getBean(name) />
</#function>
