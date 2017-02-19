<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    Main  
  </#assign>
  
  <@ui.addScript>
    <script defer="defer">
    </script>
  </@ui.addScript>
  
  <#assign navbar in ui>
    <@s.hasRole 'ROLE_ADMIN'>
      <li>
        <a href="<@f.url '/admin' />">Админка</a>
      </li>
    </@s.hasRole>
    <li>
      <a href="<@f.url '/logout' />">Выйти</a>
    </li>
  </#assign>
  
  <#assign content in ui>
    <header>
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <img class="img-responsive" src="<@u.r 'img/profile.png' />" alt="">
            <div class="intro-text">
              <span class="skills">Welcome, ${user.username}!</span>
              <hr class="star-light">
              <span class="skills">This is main page!</span>
            </div>
          </div>
        </div>
      </div>
    </header>
  </#assign>  
</@ui.composition>
