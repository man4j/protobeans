<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    <@f.i18n 'page.error.title' 'Server Error' />  
  </#assign>
    
  <#assign content in ui>
    <section id="contact">
      <div class="container">
        <div class="row">
          <div class="col-lg-12 text-center">
            <h3><@f.i18n 'page.error.title' 'Server Error' /></h3>
            <hr class="star-primary">
          </div>
        </div>
      </div>
    </section>
  </#assign>
</@ui.composition>
