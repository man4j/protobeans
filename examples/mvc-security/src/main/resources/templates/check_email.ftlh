<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    Check Email  
  </#assign>
  
  <@ui.addScript>
    <script defer="defer">
    </script>
  </@ui.addScript>
  
  <#assign navbar in ui>
    <li>
      <a href="<@f.url '/' />">Главная</a>
    </li>
  </#assign>
  
  <#assign content in ui>
    <header>
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <img style="width:200px;" class="img-responsive" src="<@u.r 'img/email3.png' />" alt="">
            <div class="intro-text">
              <span class="skills">Check Email!</span>
              <hr class="star-light">
            </div>
          </div>
        </div>
      </div>
    </header>
  </#assign>  
</@ui.composition>
