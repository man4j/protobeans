<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    Expired  
  </#assign>
  
  <@ui.addScript>
    <script defer="defer">
    </script>
  </@ui.addScript>
    
  <#assign content in ui>
    <header>
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <img class="img-responsive" style="width:100px;" src="<@u.r 'img/info.png' />" alt="">
            <div class="intro-text">
              <span class="skills"></span>
              <hr class="star-light">
              <span class="skills"><@f.messageText 'sigin.expiredLink' 'К сожалению данная ссылка уже была использована и больше не действительна.'/></span>
            </div>
          </div>
        </div>
      </div>
    </header>
  </#assign>  
</@ui.composition>
