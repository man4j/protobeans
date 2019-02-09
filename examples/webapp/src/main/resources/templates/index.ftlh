<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    <@f.i18n 'page.root.title' 'Main page'/>  
  </#assign>
    
  <#assign navbar in ui>
    <@s.hasRole 'ROLE_ADMIN'>
      <li>
        <a href="<@f.url '/admin' />"><@f.i18n 'page.admin.title' 'Admin page'/></a>
      </li>
    </@s.hasRole>
    <li>
      <a href="<@f.url '/change_password' />"><@f.i18n 'page.changepw.content' 'Change Password' /></a>
    </li>
    <li>
      <@f.form '/logout' 'logout'>
        <a style="padding-top: 15px; padding-bottom: 15px; display:block;" href="#" onclick="document.getElementById('logout').submit();"><@f.i18n 'page.logout' 'Logout'/></a>
      </@f.form>
    </li>
  </#assign>
  
  <#assign content in ui>
    <header>
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <img class="img-responsive" src="<@u.r 'img/profile.png' />" alt="">
            <div class="intro-text">
              <span class="skills"><@f.i18n 'page.welcome' 'Welcome' />, ${user.username}!</span>
              <hr class="star-light">
              <span class="skills"><@f.i18n 'page.main.content' 'This is main page'/></span>
            </div>
          </div>
        </div>
      </div>
    </header>
  </#assign>  
</@ui.composition>
