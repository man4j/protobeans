<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    Admin  
  </#assign>
  
  <@ui.addScript>
    <script defer="defer">
    </script>
  </@ui.addScript>
  
  <#assign navbar in ui>
    <li>
      <a href="<@f.url '/' />">Главная</a>
    </li>
    <li>
      <@f.form '/logout' 'logout'>
        <a style="padding-top: 15px; padding-bottom: 15px; display:block;" href="#" onclick="document.getElementById('logout').submit();">Выйти</a>
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
              <span class="skills">Welcome, ${user.username}!</span>
              <hr class="star-light">
              <span class="skills">This is admin page!</span>
            </div>
          </div>
        </div>
      </div>
    </header>
  </#assign>  
</@ui.composition>
