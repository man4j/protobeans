<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    <@f.i18n 'page.forbidden' 'Forbidden' />
  </#assign>
    
  <#assign navbar in ui>
    <li>
      <a href="<@f.url '/' />"><@f.i18n 'page.forbidden' 'Forbidden'/></a>
    </li>
  </#assign>
  
  <#assign content in ui>
    <header>
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <div class="intro-text">
              <span class="skills"><@f.i18n 'page.forbidden' 'Forbidden'/></span>
              <hr class="star-light">
            </div>
          </div>
        </div>
      </div>
    </header>
  </#assign>  
</@ui.composition>
