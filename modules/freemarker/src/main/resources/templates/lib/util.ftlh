<#import "/spring.ftl" as spring/>

<#macro errorClass fieldName err dflt="">
  <@spring.bind fieldName />
              
  <#if spring.status.error>
    <#assign errClass=err />
  <#else>
    <#assign errClass=dflt />
  </#if>
</#macro>

<#function errClass fieldName className defaultClassName="">
  <@spring.bind fieldName />
  
  <#if spring.status.error>
  	<#return className />
  <#else>
  	<#return defaultClassName />
  </#if>
</#function>

<#macro ifIsCurrentUri uri what="" other="">
  <#assign checkedUrl = rc.getContextUrl(uri) />
  
  <#if rc.requestUri == checkedUrl>
    ${what}
  <#else>
    ${other}
  </#if>
</#macro>

<#macro r path>
  <@spring.url '${getBean("freeMarkerVariables").getVariable("resourcesPrefix")}/${path}' />
</#macro>

<#function getBean name>
  <#return rc.webApplicationContext.getBean(name) />
</#function>
