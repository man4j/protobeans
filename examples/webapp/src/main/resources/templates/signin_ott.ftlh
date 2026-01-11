<@ui.composition '/base.ftlh'>  
  <#assign title in ui>
    <@f.i18n 'page.signin.title' 'SignIn page'/>    
  </#assign>
  
  <#assign content in ui>
    <section id="contact">
      <div class="container">
        <div class="row">
          <div class="col-lg-12 text-center">
            <h3 id="loginFormTitle"><@f.i18n 'page.signin.loginForm' 'Login Form'/></h3>
            <hr class="star-primary">
          </div>
        </div>
        
        <div class="row">
          <div class="col-lg-8 col-lg-offset-2">
            <@f.showErrorsBlock />
          </div>
        </div>
        
        <div class="row">
          <div class="col-lg-8 col-lg-offset-2">
            <@f.form '/signin_ott'>
              <div class="row control-group">
                <div class="form-group col-xs-12 floating-label-form-group controls">
                  <label><@f.i18n 'page.token' 'Token' /></label>
                  <@f.formInput 'form.token' 'form-control' 'err form-control' 'placeholder="${f.fi18n("page.token", "Token")}" maxlength="50" autocomplete="off"' />
                </div>
              </div>
              <div class="row">
                <div class="form-group col-xs-2">
                  <button type="submit" class="btn btn-success btn-lg" style="width:150%;"><@f.i18n 'page.signin.ok' 'OK' /></button>
                </div>
              </div>
            </@f.form>
          </div>
        </div>
      </div>
    </section>
  </#assign>
</@ui.composition>
