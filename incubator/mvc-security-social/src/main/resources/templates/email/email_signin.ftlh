<html>
  <head>

  </head>

  <body>    
    <@f.i18n 'email.checkLink' 'Click on the link please'/>:
    <a href="${baseUrl}/email_signin?uuid=${uuid?url}<#if socialUserId??>&socialUserId=${socialUserId}</#if>">${baseUrl}/auth/email?uuid=${uuid?url}<#if socialUserId??>&socialUserId=${socialUserId}</#if></a>
    <br />
    
    <#if !socialUserId??>
      <@f.i18n 'page.signin.password' 'Password'/>:
      <b>${password}</b>
    </#if>
  </body>
</html>