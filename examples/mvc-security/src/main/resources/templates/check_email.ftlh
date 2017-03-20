<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    <@f.i18n 'page.restore.title' 'Restore password' />
  </#assign>
    
  <#assign navbar in ui>
    <li>
      <a href="<@f.url '/' />"><@f.i18n 'page.root.title' 'Main page'/></a>
    </li>
  </#assign>
  
  <#assign content in ui>
    <header>
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <img style="width:200px;" class="img-responsive" src="<@u.r 'img/email3.png' />" alt="">
            <div class="intro-text">
              <span class="skills"><@f.i18n 'page.restore.success' 'Check Email!'/></span>
              <hr class="star-light">
            </div>
          </div>
        </div>
      </div>
    </header>
  </#assign>  
</@ui.composition>
