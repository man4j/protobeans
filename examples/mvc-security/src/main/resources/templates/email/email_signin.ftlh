<!DOCTYPE HTML> 
<html>
  <head>

  </head>

  <body>    
    <@f.i18n 'email.checkLink' 'Click on the link please' />:
    <a href="${baseUrl}/email_signin?uuid=${uuid?url}&email=${email?url}">${baseUrl}/email_signin?uuid=${uuid?url}&email=${email?url}</a>
    <br />
    
    <@f.i18n 'page.signin.password' 'Password'/>:
    <b>${password}</b>
  </body>
</html>